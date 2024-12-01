package com.example.medease.data.repository.auth

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