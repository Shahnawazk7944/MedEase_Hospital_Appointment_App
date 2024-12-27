package com.example.medeaseclient.data.repository.doctor

import arrow.core.Either
import com.example.medeaseclient.domain.model.Doctor
import kotlinx.coroutines.flow.Flow

interface ClientDoctorRepository {
    suspend fun fetchDoctors(): Flow<Either<DoctorsFailure, List<Doctor>>>
    suspend fun addDoctor(doctor: Doctor): Either<DoctorsFailure,DoctorsSuccess>
    suspend fun updateDoctor(doctor: Doctor): Either<DoctorsFailure,DoctorsSuccess>
    suspend fun deleteDoctor(doctorId: String): Either<DoctorsFailure,DoctorsSuccess>
}

sealed class DoctorsFailure {
    data object DataNotFound : DoctorsFailure()
    data class DatabaseError(val exception: Exception) : DoctorsFailure()
    data object NetworkError : DoctorsFailure()
    data class UnknownError(val exception: Exception) : DoctorsFailure()
}

sealed class DoctorsSuccess {
    data object DoctorAdded : DoctorsSuccess()
    data object DoctorUpdated : DoctorsSuccess()
    data object DoctorDeleted : DoctorsSuccess()
}
