package com.example.medease.data.repository

import arrow.core.Either

interface UserAuthRepository {
    suspend fun userSignUp(
        name: String, email: String, phone: String, password: String, rememberMe: Boolean
    ): Either<SignupWithEmailAndPasswordFailure, AuthSuccess>

    suspend fun userSignIn(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Either<SignInWithEmailAndPasswordFailure, AuthSuccess>

    suspend fun logout(): Either<LogoutFailure, AuthSuccess>
}

data class UserProfile(
    val name: String,
    val email: String,
    val phone: String,
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