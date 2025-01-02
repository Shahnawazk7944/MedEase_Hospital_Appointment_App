package com.example.medease.data.repository.home

import arrow.core.Either
import com.example.medease.data.repository.auth.AuthSuccess
import com.example.medease.domain.model.Bed
import com.example.medease.domain.model.HospitalWithDoctors
import com.example.medease.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserHomeRepository {
    suspend fun getUserProfile(userId: String): Either<UserProfileFailure, UserProfile>
    suspend fun logout(): Either<LogoutFailure, AuthSuccess>
    suspend fun fetchDoctorsWithHospitals(): Flow<Either<UserOperationsFailure, List<HospitalWithDoctors>>>
    suspend fun fetchBedsFromHospital(hospitalId: String): Flow<Either<UserOperationsFailure, List<Bed>>>
}

sealed class LogoutFailure {
    data class UnknownError(val exception: Exception) : LogoutFailure()
}

sealed class UserProfileFailure {
    data object UserNotFound : UserProfileFailure()
    data class NetworkError(val exception: Exception) : UserProfileFailure()
    data class DatabaseError(val exception: Exception) : UserProfileFailure()
    data class UnknownError(val exception: Exception) : UserProfileFailure()
}
sealed class UserOperationsFailure {
    data class NetworkError(val exception: Exception) : UserOperationsFailure()
    data class DatabaseError(val exception: Exception) : UserOperationsFailure()
    data class UnknownError(val exception: Exception) : UserOperationsFailure()
    data class InvalidData(val exception: Exception) : UserOperationsFailure()
    data object DataNotFound : UserOperationsFailure()
}