package com.example.medease.data.repository.home

import arrow.core.Either
import com.example.medease.data.repository.auth.AuthSuccess
import com.example.medease.domain.model.UserProfile

interface UserHomeRepository {
    suspend fun getUserProfile(userId: String): Either<UserProfileFailure, UserProfile>
    suspend fun logout(): Either<LogoutFailure, AuthSuccess>
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