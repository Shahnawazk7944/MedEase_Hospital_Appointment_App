package com.example.medease.data.repository.allFeatures

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.medease.data.firebase.FirebaseWrapper
import com.example.medease.data.util.APPOINTMENTS_COLLECTION
import com.example.medease.data.util.DOCTORS_COLLECTION
import com.example.medease.data.util.TRANSACTIONS_COLLECTION
import com.example.medease.data.util.USERS_COLLECTION
import com.example.medease.data.util.USER_HEALTH_RECORDS_COLLECTION
import com.example.medease.domain.model.AppointmentDetails
import com.example.medease.domain.model.Booking
import com.example.medease.domain.model.Doctor
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
    private val doctorDatabase = firestore.collection(DOCTORS_COLLECTION)


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
            val doctorDocumentRef = doctorDatabase.document(appointmentDetails.doctor.doctorId)
            val snapshot = doctorDocumentRef.get().await()

            if (!snapshot.exists()) {
                return UserAllFeaturesFailure.DatabaseError(Exception("Doctor not found")).left()
            }
            val existingDoctor =
                snapshot.toObject(Doctor::class.java)
                    ?: return UserAllFeaturesFailure.DatabaseError(
                        Exception("Invalid data")
                    ).left()

            val updatedAvailabilitySlots = existingDoctor.availabilitySlots.toMutableMap().apply {
                val date = appointmentDetails.bookingDate
                if (containsKey(date)) {
                    val updatedSlots = getValue(date).map { slot ->
                        if (slot.time == appointmentDetails.bookingTime) {
                            slot.copy(available = false)
                        } else {
                            slot
                        }
                    }
                    put(date, updatedSlots)
                }
            }

            val updatedDoctorAvailability = when (appointmentDetails.bookingQuota) {
                "general" -> {
                    val currentGeneralAvailability =
                        existingDoctor.generalAvailability.toIntOrNull() ?: 0
                    if (currentGeneralAvailability > 0) {
                        existingDoctor.copy(generalAvailability = (currentGeneralAvailability - 1).toString())
                    } else {
                        existingDoctor
                    }
                }

                "care" -> {
                    val currentCareAvailability = existingDoctor.careAvailability.toIntOrNull() ?: 0
                    if (currentCareAvailability > 0) {
                        existingDoctor.copy(careAvailability = (currentCareAvailability - 1).toString())
                    } else {
                        existingDoctor
                    }
                }

                "emergency" -> {
                    val currentEmergencyAvailability =
                        existingDoctor.emergencyAvailability.toIntOrNull() ?: 0
                    if (currentEmergencyAvailability > 0) {
                        existingDoctor.copy(emergencyAvailability = (currentEmergencyAvailability - 1).toString())
                    } else {
                        existingDoctor
                    }
                }

                else -> existingDoctor
            }

            val updatedDoctor =
                updatedDoctorAvailability.copy(availabilitySlots = updatedAvailabilitySlots)
            doctorDocumentRef.set(updatedDoctor).await()

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

    override suspend fun fetchAppointmentDetails(appointmentId: String): Either<UserAllFeaturesFailure, Booking> {
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
            val healthRecords =
                userDatabase.document(userId).collection(USER_HEALTH_RECORDS_COLLECTION)
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
}

