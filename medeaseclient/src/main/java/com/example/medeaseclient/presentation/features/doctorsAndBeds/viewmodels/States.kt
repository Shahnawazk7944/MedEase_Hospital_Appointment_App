package com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels

import com.example.medeaseclient.data.repository.doctor.DoctorsFailure
import com.example.medeaseclient.data.repository.doctor.DoctorsSuccess
import com.example.medeaseclient.domain.model.Doctor

data class DoctorsStates(
    val loading: Boolean = false,
    val fetchingDoctors: Boolean = false,
    val doctors: List<Doctor> = emptyList<Doctor>(),
    val doctorsSuccess: DoctorsSuccess? = null,
    val doctorsFailure: DoctorsFailure? = null,

    val doctorId: String = "",
    val hospitalId: String = "",
    val doctorName: String = "",
    val specialist: String = "",
    val treatedSymptoms: String = "",
    val experience: String = "",
    val from: String = "",
    val to: String = "",
    val genAvail: String = "",
    val careAvail: String = "",
    val emergency: String = "",
    val generalFees: String = "",
    val careFees: String = "",
    val emergencyFees: String = "",

    val doctorNameError: String? = null,
    val specialistError: String? = null,
    val treatedSymptomsError: String? = null,
    val experienceError: String? = null,
    val fromError: String? = null,
    val toError: String? = null,
    val genAvailError: String? = null,
    val careAvailError: String? = null,
    val emergencyError: String? = null,
    val generalFeesError: String? = null,
    val careFeesError: String? = null,
    val emergencyFeesError: String? = null
)

sealed class DoctorsEvents {
    data object GetDoctors : DoctorsEvents()
    data class FillDoctorForm(val doctor: Doctor) : DoctorsEvents()
    data class AddDoctor(val doctor: Doctor) : DoctorsEvents()
    data class UpdateDoctor(val doctor: Doctor) : DoctorsEvents()
    data class DeleteDoctor(val doctorId: String) : DoctorsEvents()
    data object ResetDoctorsSuccess : DoctorsEvents()
    data object ResetDoctorsFailure : DoctorsEvents()
    data class DoctorNameChanged(val newValue: String) : DoctorsEvents()
    data class SpecialistChanged(val newValue: String) : DoctorsEvents()
    data class TreatedSymptomsChanged(val newValue: String) : DoctorsEvents()
    data class ExperienceChanged(val newValue: String) : DoctorsEvents()
    data class FromChanged(val newValue: String) : DoctorsEvents()
    data class ToChanged(val newValue: String) : DoctorsEvents()
    data class GenAvailChanged(val newValue: String) : DoctorsEvents()
    data class CareAvailChanged(val newValue: String) : DoctorsEvents()
    data class EmergencyChanged(val newValue: String) : DoctorsEvents()
    data class GeneralFeesChanged(val newValue: String) : DoctorsEvents()
    data class CareFeesChanged(val newValue: String) : DoctorsEvents()
    data class EmergencyFeesChanged(val newValue: String) : DoctorsEvents()
}

fun DoctorsStates.isAddDoctorFormValid(): Boolean {
    return doctorName.isNotBlank() && specialist.isNotBlank() && experience.isNotBlank() && generalFees.isNotBlank()
            && careFees.isNotBlank() && emergencyFees.isNotBlank() && treatedSymptoms.isNotBlank() &&
            from.isNotBlank() && to.isNotBlank() && genAvail.isNotBlank() &&
            careAvail.isNotBlank() && emergency.isNotBlank() &&
            doctorNameError == null && specialistError == null && treatedSymptomsError == null && experienceError == null &&
            fromError == null && toError == null && genAvailError == null &&
            careAvailError == null && emergencyError == null && generalFeesError == null && careFeesError == null && emergencyFeesError == null
}

fun DoctorsStates.resetAddDoctorForm(): DoctorsStates {
    return this.copy(
        doctorName = "",
        specialist = "",
        experience = "",
        from = "",
        to = "",
        genAvail = "",
        careAvail = "",
        emergency = "",
        generalFees = "",
        careFees = "",
        emergencyFees = "",
        treatedSymptoms = "",
        doctorNameError = null,
        specialistError = null,
        experienceError = null,
        fromError = null,
        toError = null,
        genAvailError = null,
        careAvailError = null,
        emergencyError = null,
        treatedSymptomsError = null
    )
}
