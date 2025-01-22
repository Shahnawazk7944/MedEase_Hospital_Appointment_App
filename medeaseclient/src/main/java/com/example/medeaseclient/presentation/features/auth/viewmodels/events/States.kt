package com.example.medeaseclient.presentation.features.auth.viewmodels.events

import com.example.medeaseclient.data.repository.auth.SignInWithEmailAndPasswordFailure
import com.example.medeaseclient.data.repository.auth.SignupWithEmailAndPasswordFailure

data class SignInStates(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val forgotPasswordEmail: String = "",
    val forgotPasswordEmailError: String? = null,
    val isForgotPasswordLinkSent: Boolean = false,
    val loading: Boolean = false,
    val rememberMe: Boolean = false,
    val isSignInSuccess: Boolean = false,
    val failure: SignInWithEmailAndPasswordFailure? = null
)

data class SignUpStates(
    val hospitalName: String = "",
    val hospitalCity: String = "",
    val hospitalPinCode: String = "",
    val hospitalEmail: String = "",
    val hospitalPhone: String = "",
    val hospitalPassword: String = "",
    val hospitalConfirmPassword: String = "",
    val hospitalNameError: String? = null,
    val hospitalCityError: String? = null,
    val hospitalPinCodeError: String? = null,
    val hospitalEmailError: String? = null,
    val hospitalPhoneError: String? = null,
    val hospitalPasswordError: String? = null,
    val hospitalConfirmPasswordError: String? = null,
    val loading: Boolean = false,
    val rememberMe: Boolean = false,
    val isSignUpSuccess: Boolean = false,
    val failure: SignupWithEmailAndPasswordFailure? = null
)
