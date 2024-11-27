package com.example.medeaseclient.data.repository

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface ClientAuthRepository {
    suspend fun clientSignUp(
        hospitalName: String, hospitalEmail: String, hospitalPhone: String,
        hospitalCity: String, hospitalPinCode: String, password: String, rememberMe: Boolean
    ): Either<SignupWithEmailAndPasswordFailure, AuthSuccess>

    suspend fun clientSignIn(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Either<SignInWithEmailAndPasswordFailure, AuthSuccess>

    suspend fun logout(): Either<LogoutFailure, AuthSuccess>
}

data class ClientProfile(
    val hospitalName: String,
    val hospitalEmail: String,
    val hospitalPhone: String,
    val hospitalCity: String,
    val hospitalPinCode: String,
    val profilePicture: String,
)

data class AuthSuccess(val authenticated: Boolean)

sealed class SignupWithEmailAndPasswordFailure {
    data object InvalidEmail : SignupWithEmailAndPasswordFailure()
    data object WeakPassword : SignupWithEmailAndPasswordFailure()
    data object AccountAlreadyExists : SignupWithEmailAndPasswordFailure()
    data class UnknownError(val exception: Exception) : SignupWithEmailAndPasswordFailure()
}

sealed class SignInWithEmailAndPasswordFailure {
    data object InvalidCredentials : SignInWithEmailAndPasswordFailure()
    data class UnknownError(val exception: Exception) : SignInWithEmailAndPasswordFailure()
}

sealed class LogoutFailure {
    data class UnknownError(val exception: Exception) : LogoutFailure()
}