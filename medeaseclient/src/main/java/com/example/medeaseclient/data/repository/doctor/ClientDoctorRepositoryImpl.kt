package com.example.medeaseclient.data.repository.doctor

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.medeaseclient.data.firebase.FirebaseWrapper
import com.example.medeaseclient.data.util.BEDS_COLLECTION
import com.example.medeaseclient.data.util.DOCTORS_COLLECTION
import com.example.medeaseclient.data.util.HOSPITALS_COLLECTION
import com.example.medeaseclient.domain.model.Bed
import com.example.medeaseclient.domain.model.Doctor
import com.example.medeaseclient.domain.model.Slot
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class ClientDoctorRepositoryImpl @Inject constructor(
    private val firebaseWrapper: FirebaseWrapper,
) : ClientDoctorRepository {

    private val firestore = firebaseWrapper.firestore
    private val database = firestore.collection(DOCTORS_COLLECTION)
    private val hospitalDatabase = firestore.collection(HOSPITALS_COLLECTION)

    override suspend fun addDoctor(doctor: Doctor): Either<DoctorsFailure, DoctorsSuccess> {
        return try {
            val documentReference = database.document()
            val availabilitySlots =
                generateAvailabilitySlots(doctor.availabilityFrom, doctor.availabilityTo)
            val doctorWithSlots = doctor.copy(
                doctorId = documentReference.id,
                availabilitySlots = availabilitySlots
            )

            documentReference.set(doctorWithSlots).await()
            DoctorsSuccess.DoctorAdded.right()
        } catch (e: Exception) {
            DoctorsFailure.DatabaseError(e).left()
        }
    }

    override suspend fun updateDoctor(doctor: Doctor): Either<DoctorsFailure, DoctorsSuccess> {
        return try {
            val documentRef = database.document(doctor.doctorId)
            val snapshot = documentRef.get().await()

            if (!snapshot.exists()) {
                return DoctorsFailure.DatabaseError(Exception("Doctor not found")).left()
            }

            val existingDoctor =
                snapshot.toObject(Doctor::class.java) ?: return DoctorsFailure.DatabaseError(
                    Exception("Invalid data")
                ).left()

            // Get existing availability slots
            val existingSlots = existingDoctor.availabilitySlots.toMutableMap()

            // Generate the new date range from availabilityFrom to availabilityTo
            val newDateRange = generateDateRange(doctor.availabilityFrom, doctor.availabilityTo)

            // Create a new map with only relevant dates
            val updatedSlots = mutableMapOf<String, List<Slot>>()

            for (date in newDateRange) {
                if (existingSlots.containsKey(date)) {
                    // If the date already exists, keep the existing slots
                    updatedSlots[date] = existingSlots[date]!!
                } else {
                    // If the date is new, create empty slots
                    updatedSlots[date] = generateTimeSlots()
                }
            }

            // Update Firestore with the final slots
            val updatedDoctor = doctor.copy(availabilitySlots = updatedSlots)
            documentRef.set(updatedDoctor).await()

            DoctorsSuccess.DoctorUpdated.right()
        } catch (e: Exception) {
            DoctorsFailure.DatabaseError(e).left()
        }
    }

    private fun generateAvailabilitySlots(fromDate: String, toDate: String): Map<String, List<Slot>> {
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val startCal = Calendar.getInstance().apply { time = format.parse(fromDate)!! }
        val endCal = Calendar.getInstance().apply { time = format.parse(toDate)!! }

        val availabilityMap = mutableMapOf<String, List<Slot>>()

        while (!startCal.after(endCal)) {
            val date = format.format(startCal.time)
            availabilityMap[date] = generateTimeSlots()
            startCal.add(Calendar.DATE, 1)
        }

        return availabilityMap
    }

    private fun generateTimeSlots(): List<Slot> {
        val slots = mutableListOf<Slot>()
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())

        val cal =
            Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0) }
        for (i in 0 until 48) {
            val startTime = format.format(cal.time)
            cal.add(Calendar.MINUTE, 30)
            val endTime = format.format(cal.time)

            slots.add(Slot(time = "$startTime - $endTime", available = true))
        }
        return slots
    }

    private fun generateDateRange(fromDate: String, toDate: String): List<String> {
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val startCal = Calendar.getInstance().apply { time = format.parse(fromDate)!! }
        val endCal = Calendar.getInstance().apply { time = format.parse(toDate)!! }

        val dateList = mutableListOf<String>()
        while (!startCal.after(endCal)) {
            dateList.add(format.format(startCal.time))
            startCal.add(Calendar.DATE, 1)
        }
        return dateList
    }

    override suspend fun deleteDoctor(doctorId: String): Either<DoctorsFailure, DoctorsSuccess> {
        return try {
            database.document(doctorId).delete().await()
            DoctorsSuccess.DoctorDeleted.right()
        } catch (e: Exception) {
            DoctorsFailure.DatabaseError(e).left()
        }
    }

    override suspend fun fetchDoctors(): Flow<Either<DoctorsFailure, List<Doctor>>> {
        // TEMPORARY CODE TO UPDATE DOCTORS WITH NEW FIELDS
        /* try {
             val doctorsSnapshot: QuerySnapshot = database.get().await()
             for (document in doctorsSnapshot.documents) {
                 val doctor = document.toObject(Doctor::class.java)
                 if (doctor != null) {
                     val updatedDoctor = doctor.copy(
                         availabilityFrom = "05-02-2025",
                         availabilityTo = "06-02-2025",
                         availabilitySlots = mapOf(
                             "05-02-2025" to slotsForDay,
                             "06-02-2025" to slotsForDay
                         )
                     )
                     database.document(document.id).set(updatedDoctor).await()
                 }
             }
         } catch (e: Exception) {
             Log.e("DoctorRepository", "Error updating doctors: ${e.message}")
         }*/
        return callbackFlow {
            val listenerRegistration = database.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(DoctorsFailure.DatabaseError(error).left())
                    return@addSnapshotListener
                }

                val doctors = snapshot?.toObjects(Doctor::class.java)?.map { doctor ->
                    doctor.copy(
                        doctorId = snapshot.documents.find { it.toObject(Doctor::class.java) == doctor }?.id
                            ?: ""
                    )
                }?.toMutableList() ?: mutableListOf()

                snapshot?.documentChanges?.forEach { documentChange ->
                    when (documentChange.type) {
                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                            val doctor = documentChange.document.toObject(Doctor::class.java)
                                .copy(doctorId = documentChange.document.id)
                            doctors.removeAll { it.doctorId == doctor.doctorId } // Prevent duplicates
                            doctors.add(doctor)
                        }

                        DocumentChange.Type.REMOVED -> {
                            val doctorId = documentChange.document.id
                            doctors.removeAll { it.doctorId == doctorId }
                        }
                    }
                }

                trySend(doctors.toList().right()) // Send an immutable list
            }

            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    override suspend fun fetchBeds(): Flow<Either<DoctorsFailure, List<Bed>>> = flow {
        try {
            val bedsCollection = firestore.collection(BEDS_COLLECTION)
            val snapshot = bedsCollection.get().await()
            val beds = snapshot.toObjects(Bed::class.java).map { bed ->
                bed.copy(
                    bedId = snapshot.documents.find { it.toObject(Bed::class.java) == bed }?.id
                        ?: ""
                )
            }
            emit(beds.right())
        } catch (e: Exception) {
            emit(DoctorsFailure.DatabaseError(e).left())
        }
    }

    override suspend fun fetchBedsFromHospital(hospitalId: String): Flow<Either<DoctorsFailure, List<Bed>>> {
        return callbackFlow {
            val bedsCollection = hospitalDatabase.document(hospitalId).collection(BEDS_COLLECTION)
            val listenerRegistration = bedsCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(DoctorsFailure.DatabaseError(error).left())
                    return@addSnapshotListener
                }
                val beds = snapshot?.toObjects(Bed::class.java)?.map { bed ->
                    bed.copy(
                        bedId = snapshot.documents.find { it.toObject(Bed::class.java) == bed }?.id
                            ?: ""
                    )
                }?.toMutableList() ?: mutableListOf()

                snapshot?.documentChanges?.forEach { documentChange ->
                    when (documentChange.type) {
                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                            val bed = documentChange.document.toObject(Bed::class.java)
                                .copy(bedId = documentChange.document.id)
                            beds.removeAll { it.bedId == bed.bedId }
                            beds.add(bed)
                        }

                        DocumentChange.Type.REMOVED -> {
                            val bedId = documentChange.document.id
                            beds.removeAll { it.bedId == bedId }
                        }
                    }
                }
                trySend(beds.right())
            }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    override suspend fun addBedToHospital(
        hospitalId: String,
        bed: Bed
    ): Either<DoctorsFailure, DoctorsSuccess> {
        return try {
            val bedsCollection = hospitalDatabase.document(hospitalId).collection(BEDS_COLLECTION)
            bedsCollection.document(bed.bedId).set(bed).await()
            DoctorsSuccess.BedAdded.right()
        } catch (e: Exception) {
            DoctorsFailure.DatabaseError(e).left()
        }
    }

    override suspend fun updateBedInHospital(
        hospitalId: String,
        bed: Bed
    ): Either<DoctorsFailure, DoctorsSuccess> {
        return try {
            val bedsCollection = hospitalDatabase.document(hospitalId).collection(BEDS_COLLECTION)
            bedsCollection.document(bed.bedId).set(bed).await()
            DoctorsSuccess.BedUpdated.right()
        } catch (e: Exception) {
            DoctorsFailure.DatabaseError(e).left()
        }
    }

    override suspend fun deleteBedFromHospital(
        hospitalId: String,
        bedId: String
    ): Either<DoctorsFailure, DoctorsSuccess> {
        return try {
            val bedsCollection = hospitalDatabase.document(hospitalId).collection(BEDS_COLLECTION)
            bedsCollection.document(bedId).delete().await()
            DoctorsSuccess.BedDeleted.right()
        } catch (e: Exception) {
            DoctorsFailure.DatabaseError(e).left()
        }
    }

}

