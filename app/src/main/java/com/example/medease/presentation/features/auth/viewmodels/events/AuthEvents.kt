package com.example.medease.presentation.features.auth.viewmodels.events

import com.example.medease.data.repository.SignInWithEmailAndPasswordFailure
import com.example.medease.data.repository.SignupWithEmailAndPasswordFailure


sealed class AuthEvent {
    data class SignUpRequest(
        val name: String,
        val email: String,
        val phone: String,
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
    data class RemoveFailure(val newValue: SignInWithEmailAndPasswordFailure? = null) : SignInEvent()
    data class ClearAllFields(val newValue: Boolean) : SignInEvent()
}

sealed class SignUpEvent {
    data class NameChanged(val newValue: String) : SignUpEvent()
    data class EmailChanged(val newValue: String) : SignUpEvent()
    data class PhoneChanged(val newValue: String) : SignUpEvent()
    data class PasswordChanged(val newValue: String) : SignUpEvent()
    data class ConfirmPasswordChanged(val newValue: String) : SignUpEvent()
    data class RememberMeChanged(val newValue: Boolean) : SignUpEvent()
    data class RemoveFailure(val newValue: SignupWithEmailAndPasswordFailure? = null) : SignUpEvent()
    data class ClearAllFields(val newValue: Boolean) : SignUpEvent()
}

