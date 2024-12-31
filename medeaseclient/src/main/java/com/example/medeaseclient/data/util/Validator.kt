package com.example.medeaseclient.data.util

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Locale

data class ValidationError(val message: String)
class Validator {

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

    fun validateDoctorName(doctorName: String): ValidationError? {
        if (doctorName.isBlank()) {
            return ValidationError("Doctor name cannot be empty")
        }
        if (doctorName.length < 3) {
            return ValidationError("Doctor name must be at least 3 characters long")
        }
        if (doctorName.any { it.isDigit() }) {
            return ValidationError("Doctor name should not contain any digits")
        }
        return null
    }

    fun validateSpecialist(specialist: String): ValidationError? {
        if (specialist.isBlank()) {
            return ValidationError("Specialist cannot be empty")
        }
        if (specialist.length < 3) {
            return ValidationError("Specialist must be at least 3 characters long")
        }
        if (specialist.any { it.isDigit() }) {
            return ValidationError("Specialist should not contain any digits")
        }
        return null
    }

    fun validateTreatedSymptoms(treatedSymptoms: String): ValidationError? {
        if (treatedSymptoms.isBlank()) {
            return ValidationError("Treated symptoms cannot be empty")
        }
        return null
    }

    fun validateExperience(experience: String): ValidationError? {
        if (experience.isBlank()) {
            return ValidationError("Experience cannot be empty")
        }
        if (!experience.all { it.isDigit() }) {
            return ValidationError("Experience must contain only digits")
        }
        return null
    }

    fun validateFrom(fromDate: String, toDate: String): ValidationError? {
        if (fromDate.isBlank()) {
            return ValidationError("From Date cannot be empty")
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val fromDateObj = dateFormat.parse(fromDate)

        if (toDate.isNotBlank()) {
            val toDateObj = dateFormat.parse(toDate)
            if (fromDateObj != null) {
                if (fromDateObj.compareTo(toDateObj) == 0) {
                    return ValidationError("From and To dates cannot be the same.")
                }
                if (!fromDateObj.before(toDateObj)) {
                    return ValidationError("From Date cannot be after To Date.")
                }
            }
        }
        return null
    }

    fun validateTo(toDate: String, fromDate: String): ValidationError? {
        if (toDate.isBlank()) {
            return ValidationError("To Date cannot be empty")
        }
        if (fromDate.isNotBlank()) {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val fromDateObj = dateFormat.parse(fromDate)
            val toDateObj = dateFormat.parse(toDate)

            if (toDateObj != null) {
                if (toDateObj.compareTo(fromDateObj) == 0) {
                    return ValidationError("To and From dates cannot be the same.")
                }
                if (toDateObj.before(fromDateObj)) {
                    return ValidationError("To Date cannot be before From Date.")
                }
            }
        }
        return null
    }

    fun validateGenAvail(genAvail: String): ValidationError? {
        if (genAvail.isBlank()) {
            return ValidationError("General availability cannot be empty")
        }
        if (!genAvail.all { it.isDigit() }) {
            return ValidationError("General availability must contain only digits")
        }
        return null
    }

    fun validateCurrAvail(currAvail: String): ValidationError? {
        if (currAvail.isBlank()) {
            return ValidationError("Current availability cannot be empty")
        }
        if (!currAvail.all { it.isDigit() }) {
            return ValidationError("Current availability must contain only digits")
        }
        return null
    }

    fun validateEmergency(emergency: String): ValidationError? {
        if (emergency.isBlank()) {
            return ValidationError("Emergency contact cannot be empty")
        }
        if (!emergency.all { it.isDigit() }) {
            return ValidationError("Emergency must contain only digits")
        }
        return null
    }

    fun validatePricePerDay(price: String): ValidationError? {
        if (price.isBlank()) {
            return ValidationError("Price cannot be empty")
        }
        if (!price.all { it.isDigit() }) {
            return ValidationError("Price must contain only digits")
        }
        return null

    }

    fun validateAvailability(availability: String): ValidationError? {
        if (availability.isBlank()) {
            return ValidationError("Availability cannot be empty")
        }
        if (availability.length < 3) {
            return ValidationError("Availability must be at least 3 characters long")
        }
        if (availability.any { it.isDigit() }) {
            return ValidationError("Availability should not contain any digits")
        }
        return null
    }

    fun validateAvailableUnits(units: String): ValidationError? {
        if (units.isBlank()) {
            return ValidationError("Units cannot be empty")
        }
        if (!units.all { it.isDigit() }) {
            return ValidationError("Units must contain only digits")
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