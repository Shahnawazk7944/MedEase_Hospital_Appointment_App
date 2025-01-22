package com.example.medease.presentation.features.auth.viewmodels.events

import com.example.medease.data.repository.auth.SignInWithEmailAndPasswordFailure
import com.example.medease.data.repository.auth.SignupWithEmailAndPasswordFailure

data class SignInStates(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val forgotPasswordEmail: String = "",
    val forgotPasswordEmailError: String? = null,
    val loading: Boolean = false,
    val rememberMe: Boolean = false,
    val isSignInSuccess: Boolean = false,
    val failure: SignInWithEmailAndPasswordFailure? = null
)
data class SignUpStates(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val loading: Boolean = false,
    val rememberMe: Boolean = false,
    val isSignUpSuccess: Boolean = false,
    val failure: SignupWithEmailAndPasswordFailure? = null
)
