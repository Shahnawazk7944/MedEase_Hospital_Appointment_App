package com.example.medease.presentation.features.common

import com.example.medease.data.repository.allFeatures.UserAllFeaturesFailure
import com.example.medease.data.repository.allFeatures.UserAllFeaturesSuccess
import com.example.medease.data.repository.auth.SignInWithEmailAndPasswordFailure
import com.example.medease.data.repository.auth.SignupWithEmailAndPasswordFailure
import com.example.medease.data.repository.home.LogoutFailure
import com.example.medease.data.repository.home.UserOperationsFailure
import com.example.medease.data.repository.home.UserProfileFailure

fun getSnackbarMessage(failure: Any?): String {
    return when (failure) {
        //signUp failures
        is SignupWithEmailAndPasswordFailure.InvalidEmail -> "Invalid email address"
        is SignupWithEmailAndPasswordFailure.WeakPassword -> "Password is too weak"
        is SignupWithEmailAndPasswordFailure.AccountAlreadyExists -> "An account with this email already exists"
        is SignupWithEmailAndPasswordFailure.UnknownError -> "An unknown error occurred"
        is SignupWithEmailAndPasswordFailure.NetworkError -> "No internet connection"
        // signIn failures
        is SignInWithEmailAndPasswordFailure.InvalidCredentials -> "Invalid email or password"
        is SignInWithEmailAndPasswordFailure.UnknownError -> "An unknown error occurred"
        is SignInWithEmailAndPasswordFailure.NetworkError -> "No internet connection"
        //logout failures
        is LogoutFailure.UnknownError -> "An unknown error occurred"
        //user profile failures
        is UserProfileFailure.UserNotFound -> "User not found"
        is UserProfileFailure.NetworkError -> "No internet connection"
        is UserProfileFailure.DatabaseError -> "Database error"
        is UserProfileFailure.UnknownError -> "An unknown error occurred"

        is UserOperationsFailure.NetworkError -> "No internet connection"
        is UserOperationsFailure.DatabaseError -> "Database error"
        is UserOperationsFailure.UnknownError -> "An unknown error occurred"
        is UserOperationsFailure.InvalidData -> "Invalid data"
        is UserOperationsFailure.DataNotFound -> "Data not found"

        is UserAllFeaturesFailure.NetworkError -> "No internet connection"
        is UserAllFeaturesFailure.DatabaseError -> "Database error"
        is UserAllFeaturesFailure.UnknownError -> "An unknown error occurred"
        is UserAllFeaturesFailure.InvalidData -> "Invalid data"
        is UserAllFeaturesFailure.DataNotFound -> "Data not found"
        is UserAllFeaturesFailure.BookingNotFound -> "Booking not found"
        is UserAllFeaturesFailure.PaymentFailed -> "Payment failed, try again later"

        is UserAllFeaturesSuccess.AppointmentCancelled -> "Appointment cancelled successfully"
        is UserAllFeaturesSuccess.PaymentSuccessful -> "Payment successful"
        // Handle other failure types as needed
        else -> "An unexpected error occurred"
    }
}