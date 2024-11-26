package com.example.medease.presentation.features.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medease.data.util.AuthValidator
import com.example.medease.presentation.features.auth.viewmodels.events.AuthEvent
import com.example.medease.presentation.features.auth.viewmodels.events.SignInEvent
import com.example.medease.presentation.features.auth.viewmodels.events.SignInStates
import com.example.medease.presentation.features.auth.viewmodels.events.SignUpEvent
import com.example.medease.presentation.features.auth.viewmodels.events.SignUpStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val validator: AuthValidator
) : ViewModel() {

    private val _signInState = MutableStateFlow(SignInStates())
    val signInState = _signInState.asStateFlow()
    private val _signUpState = MutableStateFlow(SignUpStates())
    val signUpState = _signUpState.asStateFlow()


    fun authEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.SignInRequest -> {
                viewModelScope.launch {
                    signInRequest(
                        email = event.email,
                        password = event.password,
                        rememberMe = event.rememberMe
                    )
                }
            }

            is AuthEvent.SignUpRequest -> {
                viewModelScope.launch {
                    signUpRequest(
                        name = event.name,
                        email = event.email,
                        phone = event.phone,
                        password = event.password,
                        rememberMe = event.rememberMe
                    )
                }
            }
        }
    }

    fun signInEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.EmailChanged -> {
                val error = validator.validateEmail(event.newValue)
                _signInState.update {
                    it.copy(
                        email = event.newValue,
                        emailError = error?.message
                    )
                }
            }

            is SignInEvent.PasswordChanged -> {
                val error = validator.validatePassword(event.newValue)
                _signInState.update {
                    it.copy(
                        password = event.newValue,
                        passwordError = error?.message
                    )
                }
            }

            is SignInEvent.RememberMeChanged -> {
                _signInState.update {
                    it.copy(
                        rememberMe = event.newValue
                    )
                }
            }
        }
    }

    fun signUpEvent(event: SignUpEvent) {
        when (event) {

            is SignUpEvent.NameChanged -> {
                val error = validator.validateName(event.newValue)
                _signUpState.update {
                    it.copy(
                        name = event.newValue,
                        nameError = error?.message
                    )
                }
            }

            is SignUpEvent.EmailChanged -> {
                val error = validator.validateEmail(event.newValue)
                _signUpState.update {
                    it.copy(
                        email = event.newValue,
                        emailError = error?.message
                    )
                }
            }

            is SignUpEvent.PhoneChanged -> {
                val error = validator.validatePhoneNumber(event.newValue)
                _signUpState.update {
                    it.copy(
                        phone = event.newValue,
                        phoneError = error?.message
                    )
                }
            }

            is SignUpEvent.PasswordChanged -> {
                val error = validator.validatePassword(event.newValue)
                _signUpState.update {
                    it.copy(
                        password = event.newValue,
                        passwordError = error?.message
                    )
                }
            }

            is SignUpEvent.ConfirmPasswordChanged -> {
                val error = validator.validateConfirmPassword(
                    password = signUpState.value.password,
                    confirmPassword = event.newValue
                )
                _signUpState.update {
                    it.copy(
                        confirmPassword = event.newValue,
                        confirmPasswordError = error?.message
                    )
                }
            }

            is SignUpEvent.RememberMeChanged -> {
                _signUpState.update {
                    it.copy(
                        rememberMe = event.newValue
                    )
                }
            }
        }
    }

    private suspend fun signInRequest(email: String, password: String, rememberMe: Boolean) {
        _signInState.update { it.copy(loading = true) }
        delay(3000)
        _signInState.update { it.copy(loading = false) }
    }

    private suspend fun signUpRequest(
        name: String,
        email: String,
        phone: String,
        password: String,
        rememberMe: Boolean
    ) {
        _signUpState.update { it.copy(loading = true) }
        delay(3000)
        _signUpState.update { it.copy(loading = false) }

    }
}