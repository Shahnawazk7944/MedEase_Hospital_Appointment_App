package com.example.medease.presentation.features.auth.utils

import com.example.medease.presentation.features.auth.viewmodels.events.SignInStates
import com.example.medease.presentation.features.auth.viewmodels.events.SignUpStates

fun SignInStates.isSignInFormValid(): Boolean {
    return emailError == null &&
            passwordError == null &&
            email.isNotBlank() &&
            password.isNotBlank()
}

fun SignUpStates.isSignUpFormValid(): Boolean {
    return nameError == null &&
            emailError == null &&
            phoneError == null &&
            passwordError == null &&
            confirmPasswordError == null &&
            name.isNotBlank() &&
            email.isNotBlank() &&
            phone.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank()
}

fun SignInStates.reset(): SignInStates {
    return SignInStates()
}

fun SignUpStates.reset(): SignUpStates {
    return SignUpStates()
}