package com.example.medeaseclient.presentation.features.auth.utils

import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignInStates
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignUpStates

fun SignInStates.isSignInFormValid(): Boolean {
    return emailError == null &&
            passwordError == null &&
            email.isNotBlank() &&
            password.isNotBlank()
}

fun SignUpStates.isSignUpFormValid(): Boolean {
    return hospitalNameError == null &&
            hospitalCityError == null &&
            hospitalPinCodeError == null &&
            hospitalEmailError == null &&
            hospitalPhoneError == null &&
            hospitalPasswordError == null &&
            hospitalConfirmPasswordError == null &&
            hospitalName.isNotBlank() &&
            hospitalCity.isNotBlank() &&
            hospitalPinCode.isNotBlank() &&
            hospitalEmail.isNotBlank() &&
            hospitalPhone.isNotBlank() &&
            hospitalPassword.isNotBlank() &&
            hospitalConfirmPassword.isNotBlank()
}
fun SignInStates.reset(): SignInStates {
    return SignInStates()
}

fun SignUpStates.reset(): SignUpStates {
    return SignUpStates()
}