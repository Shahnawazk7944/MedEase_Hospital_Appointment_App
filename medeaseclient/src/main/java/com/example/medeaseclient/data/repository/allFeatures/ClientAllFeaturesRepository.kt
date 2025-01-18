package com.example.medeaseclient.data.repository.allFeatures

import arrow.core.Either
import com.example.medeaseclient.domain.model.AppointmentDetails
import kotlinx.coroutines.flow.Flow

interface ClientAllFeaturesRepository {
    suspend fun fetchHospitalAppointments(hospitalId: String): Flow<Either<ClientAllFeaturesFailure, List<AppointmentDetails>>>
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
    object AppointmentUpdated : ClientAllFeaturesSuccess()
}
