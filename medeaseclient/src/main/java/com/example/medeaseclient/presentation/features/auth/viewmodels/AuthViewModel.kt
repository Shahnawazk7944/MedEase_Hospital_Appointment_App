package com.example.medeaseclient.presentation.features.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medeaseclient.data.util.AuthValidator
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.AuthEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignInEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignInStates
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignUpEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignUpStates
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
                    signInRequest(email = event.email, password = event.password,rememberMe = event.rememberMe)
                }
            }

            is AuthEvent.SignUpRequest -> {
                viewModelScope.launch {
                    signUpRequest(
                        hospitalName = event.hospitalName,
                        hospitalCity = event.hospitalCity,
                        hospitalPinCode = event.hospitalPinCode,
                        hospitalEmail = event.hospitalEmail,
                        hospitalPhone = event.hospitalPhone,
                        hospitalPassword = event.password,
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
            is SignUpEvent.HospitalCityChanged -> {
                val error = validator.validateHospitalCity(event.newValue)
                _signUpState.update {
                    it.copy(
                        hospitalCity = event.newValue,
                        hospitalCityError = error?.message
                    )
                }
            }

            is SignUpEvent.HospitalConfirmPasswordChanged -> {
                val error = validator.validateConfirmPassword(
                    password = _signUpState.value.hospitalPassword,
                    confirmPassword = event.newValue
                )
                _signUpState.update {
                    it.copy(
                        hospitalConfirmPassword = event.newValue,
                        hospitalConfirmPasswordError = error?.message
                    )
                }
            }

            is SignUpEvent.HospitalEmailChanged -> {
                val error = validator.validateEmail(event.newValue)
                _signUpState.update {
                    it.copy(
                        hospitalEmail = event.newValue,
                        hospitalEmailError = error?.message
                    )
                }
            }

            is SignUpEvent.HospitalNameChanged -> {
                val error = validator.validateHospitalName(event.newValue)
                _signUpState.update {
                    it.copy(
                        hospitalName = event.newValue,
                        hospitalNameError = error?.message
                    )
                }
            }

            is SignUpEvent.HospitalPasswordChanged -> {
                val error = validator.validatePassword(event.newValue)
                _signUpState.update {
                    it.copy(
                        hospitalPassword = event.newValue,
                        hospitalPasswordError = error?.message
                    )
                }
            }

            is SignUpEvent.HospitalPhoneChanged -> {
                val error = validator.validatePhoneNumber(event.newValue)
                _signUpState.update {
                    it.copy(
                        hospitalPhone = event.newValue,
                        hospitalPhoneError = error?.message
                    )
                }
            }

            is SignUpEvent.HospitalPinCodeChanged -> {
                val error = validator.validatePinCode(event.newValue)
                _signUpState.update {
                    it.copy(
                        hospitalPinCode = event.newValue,
                        hospitalPinCodeError = error?.message
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

    private suspend fun signInRequest(email: String, password: String,rememberMe: Boolean) {
        _signInState.update { it.copy(loading = true) }
        delay(3000)
        _signInState.update { it.copy(loading = false) }
    }

    private suspend fun signUpRequest(
        hospitalName: String,
        hospitalCity: String,
        hospitalPinCode: String,
        hospitalEmail: String,
        hospitalPhone: String,
        hospitalPassword: String,
        rememberMe: Boolean
    ) {
        _signUpState.update { it.copy(loading = true) }
        delay(3000)
        _signUpState.update { it.copy(loading = false) }

    }
}