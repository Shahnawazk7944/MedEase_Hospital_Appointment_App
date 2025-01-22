package com.example.medease.data.repository.allFeatures

import android.util.Log.e
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.medease.data.firebase.FirebaseWrapper
import com.example.medease.data.util.APPOINTMENTS_COLLECTION
import com.example.medease.data.util.TRANSACTIONS_COLLECTION
import com.example.medease.data.util.USERS_COLLECTION
import com.example.medease.data.util.USER_HEALTH_RECORDS_COLLECTION
import com.example.medease.domain.model.AppointmentDetails
import com.example.medease.domain.model.Booking
import com.example.medease.domain.model.HealthRecord
import com.example.medease.domain.model.PaymentDetails
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAllFeaturesRepositoryImpl @Inject constructor(
    private val firebaseWrapper: FirebaseWrapper,
) : UserAllFeaturesRepository {

    private val firestore = firebaseWrapper.firestore
    private val appointmentDatabase = firestore.collection(APPOINTMENTS_COLLECTION)
    private val userDatabase = firestore.collection(USERS_COLLECTION)
    private val transactionDatabase = firestore.collection(TRANSACTIONS_COLLECTION)


    override suspend fun createAppointment(
        paymentDetails: PaymentDetails,
        appointmentDetails: AppointmentDetails
    ): Either<UserAllFeaturesFailure, UserAllFeaturesSuccess> {
        return try {
            val appointmentDocumentReference =
                appointmentDatabase.document(appointmentDetails.appointmentId)
            val transactionDocumentReference =
                userDatabase.document(paymentDetails.userId).collection(TRANSACTIONS_COLLECTION)
                    .document(paymentDetails.transactionId)
            appointmentDocumentReference.set(appointmentDetails).await()
            transactionDocumentReference.set(paymentDetails).await()
            UserAllFeaturesSuccess.PaymentSuccessful(
                userId = appointmentDetails.userId,
                appointmentId = appointmentDetails.appointmentId,
                transactionId = paymentDetails.transactionId
            ).right()
        } catch (e: Exception) {
            UserAllFeaturesFailure.PaymentFailed(e).left()
        }
    }

    override suspend fun fetchBookingDetails(
        appointmentId: String,
        userId: String,
        transactionId: String
    ): Either<UserAllFeaturesFailure, Booking> {
        return try {
            val appointmentDeferred =
                firebaseWrapper.firestore.collection(APPOINTMENTS_COLLECTION)
                    .document(appointmentId).get()
            val transactionDeferred =
                firebaseWrapper.firestore.collection(USERS_COLLECTION).document(userId)
                    .collection(TRANSACTIONS_COLLECTION).document(transactionId).get()

            val appointmentDetails =
                appointmentDeferred.await().toObject(AppointmentDetails::class.java)
            val paymentDetails = transactionDeferred.await().toObject(PaymentDetails::class.java)

            if (appointmentDetails != null && paymentDetails != null) {
                Booking(appointmentDetails, paymentDetails).right()
            } else {
                UserAllFeaturesFailure.BookingNotFound.left()
            }
        } catch (e: Exception) {
            UserAllFeaturesFailure.DatabaseError(e).left()
        }
    }

    override suspend fun fetchAppointmentDetails(
        appointmentId: String,
    ): Either<UserAllFeaturesFailure, Booking> {
        return try {
            val appointmentDeferred =
                appointmentDatabase.document(appointmentId).get()

            val appointmentDetails =
                appointmentDeferred.await().toObject(AppointmentDetails::class.java)

            if (appointmentDetails != null) {
                Booking(appointmentDetails).right()
            } else {
                UserAllFeaturesFailure.BookingNotFound.left()
            }
        } catch (e: Exception) {
            UserAllFeaturesFailure.DatabaseError(e).left()
        }
    }

    override suspend fun fetchMyAppointments(userId: String): Either<UserAllFeaturesFailure, List<AppointmentDetails>> {
        return try {
            val appointments = appointmentDatabase
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(AppointmentDetails::class.java)
            appointments.right()
        } catch (e: Exception) {
            UserAllFeaturesFailure.DatabaseError(e).left()
        }
    }

    override suspend fun fetchMyTransactions(userId: String): Either<UserAllFeaturesFailure, List<PaymentDetails>> {
        return try {
            val transactions = userDatabase.document(userId).collection(TRANSACTIONS_COLLECTION)
                .get()
                .await()
                .toObjects(PaymentDetails::class.java)
            transactions.right()
        } catch (e: Exception) {
            UserAllFeaturesFailure.DatabaseError(e).left()
        }
    }

    override suspend fun fetchMyHealthRecords(userId: String): Either<UserAllFeaturesFailure, List<HealthRecord>> {
        return try {
            val healthRecords = userDatabase.document(userId).collection(USER_HEALTH_RECORDS_COLLECTION)
                .get()
                .await()
                .toObjects(HealthRecord::class.java)
            healthRecords.right()
        } catch (e: Exception) {
            UserAllFeaturesFailure.DatabaseError(e).left()
        }
    }

    override suspend fun cancelAppointment(
        appointmentId: String,
        newStatus: String
    ): Either<UserAllFeaturesFailure, UserAllFeaturesSuccess> {
        return try {
            appointmentDatabase.document(appointmentId).update(mapOf("status" to newStatus)).await()
            UserAllFeaturesSuccess.AppointmentCancelled.right()
        } catch (e: Exception) {
            UserAllFeaturesFailure.DatabaseError(e).left()
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

