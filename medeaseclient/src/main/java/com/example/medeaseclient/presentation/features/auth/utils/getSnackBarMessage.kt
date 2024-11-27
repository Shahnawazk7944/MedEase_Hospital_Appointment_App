package com.example.medeaseclient.presentation.features.auth.utils

import com.example.medeaseclient.data.repository.LogoutFailure
import com.example.medeaseclient.data.repository.SignInWithEmailAndPasswordFailure
import com.example.medeaseclient.data.repository.SignupWithEmailAndPasswordFailure

fun getSnackbarMessage(failure: Any?): String {
    return when (failure) {
        is SignupWithEmailAndPasswordFailure.InvalidEmail -> "Invalid email address"
        is SignupWithEmailAndPasswordFailure.WeakPassword -> "Password is too weak"
        is SignupWithEmailAndPasswordFailure.AccountAlreadyExists -> "An account with this email already exists"
        is SignupWithEmailAndPasswordFailure.UnknownError -> "An unknown error occurred"
        is SignInWithEmailAndPasswordFailure.InvalidCredentials -> "Invalid email or password"
        is SignInWithEmailAndPasswordFailure.UnknownError -> "An unknown error occurred"
        is LogoutFailure.UnknownError -> "An unknown error occurred"
        else -> "An unexpected error occurred"
    }
}