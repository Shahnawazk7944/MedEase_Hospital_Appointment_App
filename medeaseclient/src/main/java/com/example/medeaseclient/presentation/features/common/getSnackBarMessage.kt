package com.example.medeaseclient.presentation.features.common

import com.example.medeaseclient.data.repository.auth.GetRememberMeAndIDPreferencesFailure
import com.example.medeaseclient.data.repository.doctor.SignInWithEmailAndPasswordFailure
import com.example.medeaseclient.data.repository.doctor.SignupWithEmailAndPasswordFailure
import com.example.medeaseclient.data.repository.home.ClientProfileFailure
import com.example.medeaseclient.data.repository.home.LogoutFailure

fun getSnackbarMessage(failure: Any?): String {
    return when (failure) {
        //signup failures
        is SignupWithEmailAndPasswordFailure.InvalidEmail -> "Invalid email address"
        is SignupWithEmailAndPasswordFailure.WeakPassword -> "Password is too weak"
        is SignupWithEmailAndPasswordFailure.AccountAlreadyExists -> "An account with this email already exists"
        is SignupWithEmailAndPasswordFailure.UnknownError -> "An unknown error occurred"
        is SignupWithEmailAndPasswordFailure.NetworkError -> "No internet connection"
        //login failures
        is SignInWithEmailAndPasswordFailure.InvalidCredentials -> "Invalid email or password"
        is SignInWithEmailAndPasswordFailure.UnknownError -> "An unknown error occurred"
        is SignInWithEmailAndPasswordFailure.NetworkError -> "No internet connection"
        //logout failures
        is LogoutFailure.UnknownError -> "An unknown error occurred"
        is GetRememberMeAndIDPreferencesFailure.UnknownError -> "An unknown error occurred"
        // client profile failures
        is ClientProfileFailure.UnknownError -> "An unknown error occurred"
        is ClientProfileFailure.NetworkError -> "No internet connection"
        is ClientProfileFailure.UserNotFound -> "User not found"
        is ClientProfileFailure.DatabaseError -> "Database error"
        else -> "An unexpected error occurred"
    }
}