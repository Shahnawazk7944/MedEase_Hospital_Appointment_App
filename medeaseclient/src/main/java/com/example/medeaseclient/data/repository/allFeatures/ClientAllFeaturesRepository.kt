package com.example.medeaseclient.data.repository.allFeatures

import arrow.core.Either
import com.example.medeaseclient.domain.model.AppointmentDetails
import kotlinx.coroutines.flow.Flow

interface ClientAllFeaturesRepository {
    suspend fun fetchHospitalAppointments(hospitalId: String): Flow<Either<ClientAllFeaturesFailure, List<AppointmentDetails>>>
    suspend fun changeAppointmentStatus(appointmentId: String, newStatus: String): Either<ClientAllFeaturesFailure, ClientAllFeaturesSuccess>
    suspend fun reScheduleAppointment(appointmentId: String, newDate: String, newTime: String, newStatus: String): Either<ClientAllFeaturesFailure, ClientAllFeaturesSuccess>
    suspend fun markCompletedAppointment(appointmentId: String, healthRemark: String, userId: String, newStatus: String): Either<ClientAllFeaturesFailure, ClientAllFeaturesSuccess>
}

sealed class ClientAllFeaturesFailure {
    data class NetworkError(val exception: Exception) : ClientAllFeaturesFailure()
    data class DatabaseError(val exception: Exception) : ClientAllFeaturesFailure()
    data class UnknownError(val exception: Exception) : ClientAllFeaturesFailure()
    data class FetchAppointmentsFailure(val exception: Exception) : ClientAllFeaturesFailure()
}

sealed class ClientAllFeaturesSuccess {
    object AppointmentsFetched : ClientAllFeaturesSuccess()
    object AppointmentCancelled : ClientAllFeaturesSuccess()
    object AppointmentConfirmed : ClientAllFeaturesSuccess()
    object AppointmentReScheduled : ClientAllFeaturesSuccess()
    object AppointmentCompleted : ClientAllFeaturesSuccess()
}
