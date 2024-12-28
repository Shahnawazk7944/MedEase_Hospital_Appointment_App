package com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medeaseclient.data.repository.doctor.ClientDoctorRepository
import com.example.medeaseclient.data.util.Validator
import com.example.medeaseclient.domain.model.Doctor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorsViewModel @Inject constructor(
    private val validator: Validator,
    private val clientDoctorRepository: ClientDoctorRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DoctorsStates())
    val state = _state.asStateFlow()


    fun doctorsEvents(event: DoctorsEvents) {
        when (event) {
            DoctorsEvents.GetDoctors -> {
                fetchDoctors()
            }

            is DoctorsEvents.AddDoctor -> {
                addDoctor(event.doctor)
            }

            is DoctorsEvents.DeleteDoctor -> {
                deleteDoctor(event.doctorId)
            }

            is DoctorsEvents.UpdateDoctor -> {
                updateDoctor(event.doctor)
            }

            DoctorsEvents.ResetDoctorsFailure -> {
                _state.update {
                    it.copy(
                        doctorsFailure = null
                    )
                }
            }

            DoctorsEvents.ResetDoctorsSuccess -> {
                _state.update {
                    it.copy(
                        doctorsSuccess = null
                    )
                }
            }

            is DoctorsEvents.FillDoctorForm -> {
                _state.update {it.copy(
                    doctorId = event.doctor.doctorId,
                    hospitalId = event.doctor.hospitalId,
                    doctorName = event.doctor.name,
                    specialist = event.doctor.specialist,
                    experience = event.doctor.experience,
                    from = event.doctor.availabilityFrom,
                    to = event.doctor.availabilityTo,
                    genAvail = event.doctor.generalAvailability,
                    currAvail = event.doctor.currentAvailability,
                    emergency = event.doctor.emergencyAvailability,
                )}
            }

            /****************************************************************************************/
            is DoctorsEvents.CurrAvailChanged -> {
                val error = validator.validateCurrAvail(event.newValue)
                _state.update {
                    it.copy(
                        currAvail = event.newValue,
                        currAvailError = error?.message
                    )
                }
            }

            is DoctorsEvents.DoctorNameChanged -> {
                val error = validator.validateDoctorName(event.newValue)
                _state.update {
                    it.copy(
                        doctorName = event.newValue,
                        doctorNameError = error?.message
                    )
                }
            }

            is DoctorsEvents.EmergencyChanged -> {
                val error = validator.validateEmergency(event.newValue)
                _state.update {
                    it.copy(
                        emergency = event.newValue,
                        emergencyError = error?.message
                    )
                }
            }

            is DoctorsEvents.ExperienceChanged -> {
                val error = validator.validateExperience(event.newValue)
                _state.update {
                    it.copy(
                        experience = event.newValue,
                        experienceError = error?.message
                    )
                }
            }

            is DoctorsEvents.FromChanged -> {
                val fromError = validator.validateFrom(
                    fromDate = event.newValue,
                    toDate = state.value.to
                )
                val toError = validator.validateTo(
                    fromDate = event.newValue,
                    toDate = state.value.to,
                )
                _state.update { currentState ->
                    currentState.copy(
                        from = event.newValue,
                        fromError = fromError?.message,
                        to = state.value.to,
                        toError = toError?.message,
                    )
                }
            }

            is DoctorsEvents.GenAvailChanged -> {
                val error = validator.validateGenAvail(event.newValue)
                _state.update {
                    it.copy(
                        genAvail = event.newValue,
                        genAvailError = error?.message
                    )
                }
            }

            is DoctorsEvents.SpecialistChanged -> {
                val error = validator.validateSpecialist(event.newValue)
                _state.update {
                    it.copy(
                        specialist = event.newValue,
                        specialistError = error?.message
                    )
                }
            }

            is DoctorsEvents.ToChanged -> {
                val toError = validator.validateTo(
                    toDate = event.newValue,
                    fromDate = state.value.from
                )
                val fromError = validator.validateFrom(
                    toDate = event.newValue,
                    fromDate = state.value.from
                )
                _state.update { currentState ->
                    currentState.copy(
                        to = event.newValue,
                        toError = toError?.message,
                        from = state.value.from,
                        fromError = fromError?.message
                    )
                }
            }
            /****************************************************************************************/
        }
    }

    private fun fetchDoctors() {
        _state.update {
            it.copy(
                fetchingDoctors = true
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            clientDoctorRepository.fetchDoctors().collect {
                it.onRight { doctors ->
                    delay(500)
                    _state.update {
                        it.copy(
                            doctors = doctors,
                            fetchingDoctors = false
                        )
                    }
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            fetchingDoctors = false,
                            doctorsFailure = failure
                        )
                    }
                }
            }
        }
    }

    private fun addDoctor(doctor: Doctor) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientDoctorRepository.addDoctor(doctor).onRight { doctorAdded ->
                delay(1000)
                _state.update { it.copy(loading = false, doctorsSuccess = doctorAdded) }
                _state.update { it.resetAddDoctorForm() }
            }.onLeft { failure ->
                _state.update {
                    it.copy(
                        loading = false,
                        doctorsFailure = failure
                    )
                }
            }
        }
    }

    private fun updateDoctor(doctor: Doctor) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientDoctorRepository.updateDoctor(doctor).onRight { doctorUpdated ->
                delay(1000)
                _state.update { it.copy(loading = false, doctorsSuccess = doctorUpdated) }
                _state.update { it.resetAddDoctorForm() }
            }.onLeft { failure ->
                _state.update {
                    it.copy(
                        loading = false,
                        doctorsFailure = failure
                    )
                }
            }
        }
    }

    private fun deleteDoctor(doctorId: String) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientDoctorRepository.deleteDoctor(doctorId).onRight { doctorDeleted ->
                delay(1000)
                _state.update { it.copy(loading = false, doctorsSuccess = doctorDeleted) }
            }.onLeft { failure ->
                _state.update {
                    it.copy(
                        loading = false,
                        doctorsFailure = failure
                    )
                }
            }
        }
    }

}
