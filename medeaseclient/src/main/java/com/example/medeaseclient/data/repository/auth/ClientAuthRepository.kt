package com.example.medeaseclient.data.repository.auth

import arrow.core.Either

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

    suspend fun clientForgotPassword(
        email: String,
    ): Either<SignInWithEmailAndPasswordFailure, Unit>
}

data class AuthSuccess(val authenticated: Boolean)

sealed class SignupWithEmailAndPasswordFailure {
    data object InvalidEmail : SignupWithEmailAndPasswordFailure()
    data object WeakPassword : SignupWithEmailAndPasswordFailure()
    data object AccountAlreadyExists : SignupWithEmailAndPasswordFailure()
    data object NetworkError : SignupWithEmailAndPasswordFailure()
    data class UnknownError(val exception: Exception) : SignupWithEmailAndPasswordFailure()
}

sealed class SignInWithEmailAndPasswordFailure {
    data object InvalidCredentials : SignInWithEmailAndPasswordFailure()
    data object NetworkError : SignInWithEmailAndPasswordFailure()
    data class UnknownError(val exception: Exception) : SignInWithEmailAndPasswordFailure()
}