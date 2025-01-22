package com.example.medease.data.repository.allFeatures

import arrow.core.Either
import com.example.medease.domain.model.AppointmentDetails
import com.example.medease.domain.model.Booking
import com.example.medease.domain.model.HealthRecord
import com.example.medease.domain.model.PaymentDetails

interface UserAllFeaturesRepository {
    suspend fun createAppointment(
        paymentDetails: PaymentDetails,
        appointmentDetails: AppointmentDetails
    ): Either<UserAllFeaturesFailure, UserAllFeaturesSuccess>

    suspend fun fetchBookingDetails(
        appointmentId: String,
        userId: String,
        transactionId: String
    ): Either<UserAllFeaturesFailure, Booking>

    suspend fun fetchAppointmentDetails(
        appointmentId: String,
    ): Either<UserAllFeaturesFailure, Booking>

    suspend fun fetchMyAppointments(userId: String): Either<UserAllFeaturesFailure, List<AppointmentDetails>>
    suspend fun fetchMyTransactions(userId: String): Either<UserAllFeaturesFailure, List<PaymentDetails>>
    suspend fun fetchMyHealthRecords(userId: String): Either<UserAllFeaturesFailure, List<HealthRecord>>
    suspend fun cancelAppointment(
        appointmentId: String,
        newStatus: String
    ): Either<UserAllFeaturesFailure, UserAllFeaturesSuccess>
}

sealed class UserAllFeaturesFailure {
    data object DataNotFound : UserAllFeaturesFailure()
    data class DatabaseError(val exception: Exception) : UserAllFeaturesFailure()
    data object NetworkError : UserAllFeaturesFailure()
    data class UnknownError(val exception: Exception) : UserAllFeaturesFailure()
    data class InvalidData(val exception: Exception) : UserAllFeaturesFailure()
    data object BookingNotFound : UserAllFeaturesFailure()
    data class PaymentFailed(val exception: Exception) : UserAllFeaturesFailure()

}

sealed class UserAllFeaturesSuccess {
    object AppointmentCancelled : UserAllFeaturesSuccess()
    data class PaymentSuccessful(
        val transactionId: String,
        val userId: String,
        val appointmentId: String
    ) : UserAllFeaturesSuccess()
}