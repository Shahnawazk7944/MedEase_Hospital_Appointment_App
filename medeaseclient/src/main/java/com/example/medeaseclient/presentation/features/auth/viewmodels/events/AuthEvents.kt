package com.example.medeaseclient.presentation.features.auth.viewmodels.events


sealed class AuthEvent {
    data class SignUpRequest(
        val hospitalName: String,
        val hospitalEmail: String,
        val hospitalPhone: String,
        val hospitalCity: String,
        val hospitalPinCode: String,
        val password: String,
        val rememberMe: Boolean
    ) : AuthEvent()

    data class SignInRequest(
        val email: String,
        val password: String,
        val rememberMe: Boolean
    ) : AuthEvent()
}
sealed class SignInEvent {
    data class EmailChanged(val newValue: String) : SignInEvent()
    data class PasswordChanged(val newValue: String) : SignInEvent()
    data class RememberMeChanged(val newValue: Boolean) : SignInEvent()
}
sealed class SignUpEvent {
    data class HospitalNameChanged(val newValue: String) : SignUpEvent()
    data class HospitalEmailChanged(val newValue: String) : SignUpEvent()
    data class HospitalPhoneChanged(val newValue: String) : SignUpEvent()
    data class HospitalCityChanged(val newValue: String) : SignUpEvent()
    data class HospitalPinCodeChanged(val newValue: String) : SignUpEvent()
    data class HospitalPasswordChanged(val newValue: String) : SignUpEvent()
    data class HospitalConfirmPasswordChanged(val newValue: String) : SignUpEvent()
    data class RememberMeChanged(val newValue: Boolean) : SignUpEvent()
}

