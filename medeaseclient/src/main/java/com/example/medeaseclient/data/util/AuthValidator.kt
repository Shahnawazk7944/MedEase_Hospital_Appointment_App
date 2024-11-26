package com.example.medeaseclient.data.util

import android.R.attr.phoneNumber
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.Pattern

data class ValidationError(val message: String)
class AuthValidator {

    fun validateHospitalName(hospitalName: String): ValidationError? {
        if (hospitalName.isBlank()) {
            return ValidationError(
                "Hospital name cannot be empty"
            )
        }
        if (hospitalName.length < 3) {
            return ValidationError(
                "Hospital name must be at least 3 characters long"
            )
        }
        if (hospitalName.any { it.isDigit() }) {
            return ValidationError(
                "Hospital name should not contain any digits"
            )
        }
        return null
    }
    fun validateHospitalCity(hospitalCity: String): ValidationError? {
        if (hospitalCity.isBlank()) {
            return ValidationError(
                "Hospital city cannot be empty"
            )

        }
        if (hospitalCity.length < 3) {
            return ValidationError(
                "Hospital city must be at least 3 characters long"
            )
        }
        if (hospitalCity.any { it.isDigit() }) {
            return ValidationError(
                "Hospital city should not contain any digits"
            )
        }
        return null
    }
    fun validateEmail(email: String): ValidationError? {
        if (email.isBlank()) {
            return ValidationError(
                "Email cannot be empty"
            )
        }
        if (!email.isValidEmail()) {
            return ValidationError(
                "Invalid email format"
            )
        }
        return null
    }
    fun validatePhoneNumber(phoneNumber: String): ValidationError? {
        if (phoneNumber.isBlank()) {
            return ValidationError(
                "Phone number cannot be empty"
            )
        }
        if (phoneNumber.length < 10 || phoneNumber.length > 13) {
            return ValidationError(
                "Phone number must be between 10 and 13 digits"
            )
        }
        if (!phoneNumber.isValidPhoneNumber()) {
            return ValidationError(
                "Invalid phone number format"
            )
        }
        return null
    }
    fun validatePinCode(pinCode: String): ValidationError? {
        if (pinCode.isBlank()) {
            return ValidationError(
                "Pin code cannot be empty"
            )
        }
        if (pinCode.length < 6 || pinCode.length > 8) {
            return ValidationError(
                "Pin code must be between 6 and 8 digits"
            )
        }
        if (!pinCode.all { it.isDigit() }) {
            return ValidationError(
                "Pin code must contain only digits"
            )
        }
        return null
    }
    fun validatePassword(password: String): ValidationError? {
        if (password.isBlank()) {
            return ValidationError(
                "Password cannot be empty"
            )
        }
        if (password.length < 8) {
            return ValidationError(
                "Password must be at least 8 characters long"
            )
        }
        if (!password.any { it.isDigit() }) {
            return ValidationError(
                "Password must contain at least one digit"
            )
        }
        if (!password.any { it.isUpperCase() }) {
            return ValidationError(
                "Password must contain at least one uppercase letter"
            )
        }
        if (!password.any { it.isLowerCase() }) {
            return ValidationError(
                "Password must contain at least one lowercase letter"
            )
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            return ValidationError(
                "Password must contain at least one special character"
            )
        }
        return null
    }
    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationError? {
        if (confirmPassword.isBlank()) {
            return ValidationError(
                "Confirm password cannot be empty"
            )
        }
        if (password != confirmPassword) {
            return ValidationError(
                "Passwords do not match"
            )
        }
        return null
    }

    private fun String.isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun String.isValidPhoneNumber(): Boolean {
        return Patterns.PHONE.matcher(this).matches()
    }
}