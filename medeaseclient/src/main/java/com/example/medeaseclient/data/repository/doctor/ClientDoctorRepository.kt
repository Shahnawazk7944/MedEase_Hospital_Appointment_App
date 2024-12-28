package com.example.medeaseclient.data.repository.doctor

import arrow.core.Either
import com.example.medeaseclient.domain.model.Bed
import com.example.medeaseclient.domain.model.Doctor
import kotlinx.coroutines.flow.Flow

interface ClientDoctorRepository {
    suspend fun fetchDoctors(): Flow<Either<DoctorsFailure, List<Doctor>>>
    suspend fun addDoctor(doctor: Doctor): Either<DoctorsFailure,DoctorsSuccess>
    suspend fun updateDoctor(doctor: Doctor): Either<DoctorsFailure,DoctorsSuccess>
    suspend fun deleteDoctor(doctorId: String): Either<DoctorsFailure,DoctorsSuccess>
    suspend fun fetchBeds(): Flow<Either<DoctorsFailure, List<Bed>>>
    suspend fun fetchBedsFromHospital(hospitalId: String): Flow<Either<DoctorsFailure, List<Bed>>>
    suspend fun addBedToHospital(hospitalId: String, bed: Bed): Either<DoctorsFailure,DoctorsSuccess>
    suspend fun updateBedInHospital(hospitalId: String, bed: Bed): Either<DoctorsFailure,DoctorsSuccess>
    suspend fun deleteBedFromHospital(hospitalId: String, bedId: String): Either<DoctorsFailure,DoctorsSuccess>
}

sealed class DoctorsFailure {
    data object DataNotFound : DoctorsFailure()
    data class DatabaseError(val exception: Exception) : DoctorsFailure()
    data object NetworkError : DoctorsFailure()
    data class UnknownError(val exception: Exception) : DoctorsFailure()
    data class InvalidData(val exception: Exception) : DoctorsFailure()
}

sealed class DoctorsSuccess {
    data object DoctorAdded : DoctorsSuccess()
    data object DoctorUpdated : DoctorsSuccess()
    data object DoctorDeleted : DoctorsSuccess()
    data object BedAdded : DoctorsSuccess()
    data object BedUpdated : DoctorsSuccess()
    data object BedDeleted : DoctorsSuccess()
}
