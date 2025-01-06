package com.example.medease.data.util

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Locale

data class ValidationError(val message: String)
class Validator {

    fun validateName(name: String): ValidationError? {
        if (name.isBlank()) {
            return ValidationError(
                "Name cannot be empty"
            )
        }
        if (name.length < 3) {
            return ValidationError(
                "Name must be at least 3 characters long"
            )
        }
        if (name.any { it.isDigit() }) {
            return ValidationError(
                "Name should not contain any digits"
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

    fun validateBookingDate(bookingDate: String, fromDate: String, toDate: String): ValidationError? {
        if (bookingDate.isBlank()) {
            return ValidationError("Booking Date cannot be empty")
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val bookingDateObj = dateFormat.parse(bookingDate)

        val fromDateObj = dateFormat.parse(fromDate)
        val toDateObj = dateFormat.parse(toDate)

        if (bookingDateObj != null && fromDateObj != null && toDateObj != null) {
            if (bookingDateObj.before(fromDateObj) || bookingDateObj.after(toDateObj)) {
                return ValidationError("Booking Date must be between From Date and To Date.")
            }
        } else {
            return ValidationError("Invalid date format. Please use dd-MM-yyyy.")
        }

        return null
    }

    fun validateBookingTime(bookingTime: String): ValidationError? {
        if (bookingTime.isBlank()) {
            return ValidationError("Booking Time cannot be empty")
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