package com.example.medeaseclient.data.repository.home

import arrow.core.Either
import com.example.medeaseclient.data.repository.auth.AuthSuccess
import com.example.medeaseclient.domain.model.ClientProfile

interface ClientHomeRepository {
    suspend fun getClientProfile(clientId: String): Either<ClientProfileFailure, ClientProfile>
    suspend fun logout(): Either<LogoutFailure, AuthSuccess>
}

sealed class LogoutFailure {
    data class UnknownError(val exception: Exception) : LogoutFailure()
}

sealed class ClientProfileFailure {
    data object UserNotFound : ClientProfileFailure()
    data class NetworkError(val exception: Exception) : ClientProfileFailure()
    data class DatabaseError(val exception: Exception) : ClientProfileFailure()
    data class UnknownError(val exception: Exception) : ClientProfileFailure()
}
