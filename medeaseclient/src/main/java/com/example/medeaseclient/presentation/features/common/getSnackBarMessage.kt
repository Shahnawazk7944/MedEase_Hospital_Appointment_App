package com.example.medeaseclient.presentation.features.common

import com.example.medeaseclient.data.repository.allFeatures.ClientAllFeaturesFailure
import com.example.medeaseclient.data.repository.auth.GetRememberMeAndIDPreferencesFailure
import com.example.medeaseclient.data.repository.auth.SignInWithEmailAndPasswordFailure
import com.example.medeaseclient.data.repository.auth.SignupWithEmailAndPasswordFailure
import com.example.medeaseclient.data.repository.doctor.DoctorsFailure
import com.example.medeaseclient.data.repository.doctor.DoctorsSuccess
import com.example.medeaseclient.data.repository.home.ClientProfileFailure
import com.example.medeaseclient.data.repository.home.LogoutFailure

fun getSnackbarToastMessage(failure: Any?): String {
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

        is DoctorsFailure.UnknownError -> "An unknown error occurred"
        is DoctorsFailure.NetworkError -> "No internet connection"
        is DoctorsFailure.DataNotFound -> "Data not found"
        is DoctorsFailure.DatabaseError -> "Database error"
        is DoctorsFailure.InvalidData -> "Invalid data"

        is DoctorsSuccess.DoctorAdded -> "Doctor added successfully"
        is DoctorsSuccess.DoctorUpdated -> "Doctor updated successfully"
        is DoctorsSuccess.DoctorDeleted -> "Doctor deleted successfully"
        is DoctorsSuccess.BedAdded -> "Bed added successfully"
        is DoctorsSuccess.BedUpdated -> "Bed updated successfully"
        is DoctorsSuccess.BedDeleted -> "Bed deleted successfully"

        is ClientAllFeaturesFailure.UnknownError -> "An unknown error occurred"
        is ClientAllFeaturesFailure.NetworkError -> "No internet connection"
        is ClientAllFeaturesFailure.DatabaseError -> "Database error"
        is ClientAllFeaturesFailure.FetchAppointmentsFailure -> "Failed to fetch appointments"
        else -> "An unexpected error occurred"
    }
}