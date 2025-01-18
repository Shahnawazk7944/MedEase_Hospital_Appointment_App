package com.example.medeaseclient.data.repository.allFeatures

import android.util.Log
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.medeaseclient.data.firebase.FirebaseWrapper
import com.example.medeaseclient.data.util.APPOINTMENTS_COLLECTION
import com.example.medeaseclient.domain.model.AppointmentDetails
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ClientAllFeaturesRepositoryImpl @Inject constructor(
    private val firebaseWrapper: FirebaseWrapper,
) : ClientAllFeaturesRepository {

    private val firestore = firebaseWrapper.firestore
    private val appointmentDatabase = firestore.collection(APPOINTMENTS_COLLECTION)

    override suspend fun fetchHospitalAppointments(hospitalId: String): Flow<Either<ClientAllFeaturesFailure, List<AppointmentDetails>>> {
        return callbackFlow {
            Log.d("---repo",hospitalId)
            val listenerRegistration = appointmentDatabase.whereEqualTo("hospitalId", hospitalId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ClientAllFeaturesFailure.DatabaseError(error).left())
                        return@addSnapshotListener
                    }
                    val appointments =
                        snapshot?.toObjects(AppointmentDetails::class.java)?.map { appointment ->
                            appointment.copy(
                                appointmentId = snapshot.documents.find {
                                    it.toObject(
                                        AppointmentDetails::class.java
                                    ) == appointment
                                }?.id
                                    ?: ""
                            )
                        }?.toMutableList() ?: mutableListOf()
                    snapshot?.documentChanges?.forEach { documentChange ->
                        when (documentChange.type) {
                            DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                                val appointment =
                                    documentChange.document.toObject(AppointmentDetails::class.java)
                                        .copy(appointmentId = documentChange.document.id)
                                appointments.removeAll { it.appointmentId == appointment.appointmentId }
                                appointments.add(appointment)
                            }

                            DocumentChange.Type.REMOVED -> {
                                val appointmentId = documentChange.document.id
                                appointments.removeAll { it.appointmentId == appointmentId }
                            }
                        }
                    }
                    trySend(appointments.right())
                }
            awaitClose { listenerRegistration.remove() }
        }
    }

//    override suspend fun updateDoctor(doctor: Doctor): Either<DoctorsFailure, DoctorsSuccess> {
//        return try {
//            database.document(doctor.doctorId).set(doctor).await()
//            DoctorsSuccess.DoctorUpdated.right()
//        } catch (e: Exception) {
//            DoctorsFailure.DatabaseError(e).left()
//        }
//    }
//
//    override suspend fun deleteDoctor(doctorId: String): Either<DoctorsFailure, DoctorsSuccess> {
//        return try {
//            database.document(doctorId).delete().await()
//            DoctorsSuccess.DoctorDeleted.right()
//        } catch (e: Exception) {
//            DoctorsFailure.DatabaseError(e).left()
//        }
//    }
//
//    override suspend fun fetchDoctors(): Flow<Either<DoctorsFailure, List<Doctor>>> {
//
//        // TEMPORARY CODE TO UPDATE DOCTORS WITH NEW FIELDS
//        /*try {
//            val doctorsSnapshot: QuerySnapshot = database.get().await()
//            for (document in doctorsSnapshot.documents) {
//                val doctor = document.toObject(Doctor::class.java)
//                if (doctor != null) {
//                    val updatedDoctor = doctor.copy(
//                        generalFees = "0",
//                        careFees = "0",
//                        emergencyFees = "0"
//                    )
//                    database.document(document.id).set(updatedDoctor).await()
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("DoctorRepository", "Error updating doctors: ${e.message}")
//        }*/
//        return callbackFlow {
//            val listenerRegistration = database.addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    trySend(DoctorsFailure.DatabaseError(error).left())
//                    return@addSnapshotListener
//                }
//
//                val doctors = snapshot?.toObjects(Doctor::class.java)?.map { doctor ->
//                    doctor.copy(
//                        doctorId = snapshot.documents.find { it.toObject(Doctor::class.java) == doctor }?.id
//                            ?: ""
//                    )
//                }?.toMutableList() ?: mutableListOf()
//
//                snapshot?.documentChanges?.forEach { documentChange ->
//                    when (documentChange.type) {
//                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
//                            val doctor = documentChange.document.toObject(Doctor::class.java)
//                                .copy(doctorId = documentChange.document.id)
//                            doctors.removeAll { it.doctorId == doctor.doctorId } // Prevent duplicates
//                            doctors.add(doctor)
//                        }
//
//                        DocumentChange.Type.REMOVED -> {
//                            val doctorId = documentChange.document.id
//                            doctors.removeAll { it.doctorId == doctorId }
//                        }
//                    }
//                }
//
//                trySend(doctors.toList().right()) // Send an immutable list
//            }
//
//            awaitClose {
//                listenerRegistration.remove()
//            }
//        }
//    }
//
//    override suspend fun fetchBeds(): Flow<Either<DoctorsFailure, List<Bed>>> = flow {
//        try {
//            val bedsCollection = firestore.collection(BEDS_COLLECTION)
//            val snapshot = bedsCollection.get().await()
//            val beds = snapshot.toObjects(Bed::class.java).map { bed ->
//                bed.copy(
//                    bedId = snapshot.documents.find { it.toObject(Bed::class.java) == bed }?.id
//                        ?: ""
//                )
//            }
//            emit(beds.right())
//        } catch (e: Exception) {
//            emit(DoctorsFailure.DatabaseError(e).left())
//        }
//    }
//
//    override suspend fun fetchBedsFromHospital(hospitalId: String): Flow<Either<DoctorsFailure, List<Bed>>> {
//        return callbackFlow {
//            val bedsCollection = hospitalDatabase.document(hospitalId).collection(BEDS_COLLECTION)
//            val listenerRegistration = bedsCollection.addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    trySend(DoctorsFailure.DatabaseError(error).left())
//                    return@addSnapshotListener
//                }
//                val beds = snapshot?.toObjects(Bed::class.java)?.map { bed ->
//                    bed.copy(
//                        bedId = snapshot.documents.find { it.toObject(Bed::class.java) == bed }?.id
//                            ?: ""
//                    )
//                }?.toMutableList() ?: mutableListOf()
//
//                snapshot?.documentChanges?.forEach { documentChange ->
//                    when (documentChange.type) {
//                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
//                            val bed = documentChange.document.toObject(Bed::class.java)
//                                .copy(bedId = documentChange.document.id)
//                            beds.removeAll { it.bedId == bed.bedId }
//                            beds.add(bed)
//                        }
//
//                        DocumentChange.Type.REMOVED -> {
//                            val bedId = documentChange.document.id
//                            beds.removeAll { it.bedId == bedId }
//                        }
//                    }
//                }
//                trySend(beds.right())
//            }
//            awaitClose {
//                listenerRegistration.remove()
//            }
//        }
//    }
//
//    override suspend fun addBedToHospital(hospitalId: String, bed: Bed): Either<DoctorsFailure, DoctorsSuccess> {
//        return try {
//            val bedsCollection = hospitalDatabase.document(hospitalId).collection(BEDS_COLLECTION)
//            bedsCollection.document(bed.bedId).set(bed).await()
//            DoctorsSuccess.BedAdded.right()
//        } catch (e: Exception) {
//            DoctorsFailure.DatabaseError(e).left()
//        }
//    }
//
//    override suspend fun updateBedInHospital(hospitalId: String, bed: Bed): Either<DoctorsFailure, DoctorsSuccess> {
//        return try {
//            val bedsCollection = hospitalDatabase.document(hospitalId).collection(BEDS_COLLECTION)
//            bedsCollection.document(bed.bedId).set(bed).await()
//            DoctorsSuccess.BedUpdated.right()
//        } catch (e: Exception) {
//            DoctorsFailure.DatabaseError(e).left()
//        }
//    }
//
//    override suspend fun deleteBedFromHospital(hospitalId: String, bedId: String): Either<DoctorsFailure, DoctorsSuccess> {
//        return try {
//            val bedsCollection = hospitalDatabase.document(hospitalId).collection(BEDS_COLLECTION)
//            bedsCollection.document(bedId).delete().await()
//            DoctorsSuccess.BedDeleted.right()
//        } catch (e: Exception) {
//            DoctorsFailure.DatabaseError(e).left()
//        }
//    }

}

