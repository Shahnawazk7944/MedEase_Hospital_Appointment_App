package com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medeaseclient.data.repository.doctor.ClientDoctorRepository
import com.example.medeaseclient.data.repository.doctor.DoctorsFailure
import com.example.medeaseclient.data.repository.doctor.DoctorsSuccess
import com.example.medeaseclient.data.util.Validator
import com.example.medeaseclient.domain.model.Bed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BedsViewModel @Inject constructor(
    private val validator: Validator,
    private val clientDoctorRepository: ClientDoctorRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BedsStates())
    val state = _state.asStateFlow()


    fun bedsEvents(event: BedsEvents) {
        when (event) {
            is BedsEvents.FillBedsForm -> {
                _state.update {
                    it.copy(
                        bedId = event.bed.bedId,
                        bedType = event.bed.bedType,
                        purpose = event.bed.purpose,
                        features = event.bed.features,
                        perDayBedPriceINR = event.bed.perDayBedPriceINR,
                        availability = event.bed.availability,
                        availableUnits = event.bed.availableUnits,
                    )
                }
            }

            is BedsEvents.AddBed -> {
                addBed(event.hospitalId, event.bed)
            }

            is BedsEvents.DeleteBed -> {
                deleteBed(event.hospitalId, event.bedId)
            }

            is BedsEvents.UpdateBed -> {
                updateBed(event.hospitalId, event.bed)
            }

            is BedsEvents.GetBedsFromHospital -> {
                fetchBedsFromHospital(event.hospitalId)
            }

            BedsEvents.GetBeds -> {
                fetchBeds()
            }

            BedsEvents.ResetBedsFailure -> {
                _state.update {
                    it.copy(
                        bedsFailure = null
                    )
                }
            }

            BedsEvents.ResetBedsSuccess -> {
                _state.update {
                    it.copy(
                        bedsSuccess = null
                    )
                }
            }

            is BedsEvents.AvailabilityChanged -> {
                val error = validator.validateAvailability(event.newValue)
                _state.update {
                    it.copy(availability = event.newValue, availabilityError = error?.message)
                }
            }
            is BedsEvents.AvailableUnitsChanged -> {
                val error = validator.validateAvailableUnits(event.newValue)
                _state.update {
                    it.copy(availableUnits = event.newValue, availableUnitsError = error?.message)
                }
            }
            is BedsEvents.PerDayPriceChanged ->{
                val error = validator.validatePricePerDay(event.newValue)
                _state.update {
                    it.copy(perDayBedPriceINR = event.newValue, perDayBedPriceINRError = error?.message)
                }
            }
        }
    }

    private fun fetchBeds() {
        _state.update {
            it.copy(
                loading = true
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            clientDoctorRepository.fetchBeds().collect {
                it.onRight { beds ->
                    _state.update {
                        it.copy(
                            beds = beds,
                            loading = false
                        )
                    }
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            loading = false,
                            bedsFailure = failure
                        )
                    }
                }
            }
        }
    }

    private fun fetchBedsFromHospital(hospitalId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            clientDoctorRepository.fetchBedsFromHospital(hospitalId = hospitalId).collect {
                it.onRight { beds ->
                    _state.update {
                        it.copy(
                            bedsFromHospital = beds,
                            loading = false
                        )
                    }
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            loading = false,
                            bedsFromHospitalFailure = failure
                        )
                    }
                }
            }
        }
    }

    private fun addBed(hospitalId: String, bed: Bed) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientDoctorRepository.addBedToHospital(hospitalId = hospitalId, bed = bed)
                .onRight { bedAdded ->
                    delay(1000)
                    _state.update { it.copy(loading = false, bedsSuccess = bedAdded) }
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            loading = false,
                            bedsFailure = failure
                        )
                    }
                }
        }
    }

    private fun updateBed(hospitalId: String, bed: Bed) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientDoctorRepository.updateBedInHospital(hospitalId = hospitalId, bed = bed)
                .onRight { bedUpdated ->
                    delay(1000)
                    _state.update { it.copy(loading = false, bedsSuccess = bedUpdated) }
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            loading = false,
                            bedsFailure = failure
                        )
                    }
                }
        }
    }

    private fun deleteBed(hospitalId: String, bedId: String) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientDoctorRepository.deleteBedFromHospital(hospitalId = hospitalId, bedId = bedId)
                .onRight { bedDeleted ->
                    delay(1000)
                    _state.update { it.copy(loading = false, bedsSuccess = bedDeleted) }
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            loading = false,
                            bedsFailure = failure
                        )
                    }
                }
        }
    }
}

data class BedsStates(
    val loading: Boolean = false,
    val fetchingBeds: Boolean = false,
    val beds: List<Bed> = emptyList<Bed>(),
    val bedsFromHospital: List<Bed> = emptyList<Bed>(),
    val bedsSuccess: DoctorsSuccess? = null,
    val bedsFailure: DoctorsFailure? = null,
    val bedsFromHospitalFailure: DoctorsFailure? = null,

    val bedId: String = "",
    val bedType: String = "",
    val purpose: String = "",
    val features: List<String> = emptyList(),
    val perDayBedPriceINR: String = "",
    val availability: String = "",
    val availableUnits: String = "",
    val perDayBedPriceINRError: String? = null,
    val availabilityError: String? = null,
    val availableUnitsError: String? = null,
)

sealed class BedsEvents {
    data object GetBeds : BedsEvents()
    data class GetBedsFromHospital(val hospitalId: String) : BedsEvents()
    data class FillBedsForm(val bed: Bed) : BedsEvents()
    data class AddBed(val hospitalId: String, val bed: Bed) : BedsEvents()
    data class UpdateBed(val hospitalId: String, val bed: Bed) : BedsEvents()
    data class DeleteBed(val hospitalId: String, val bedId: String) : BedsEvents()
    data object ResetBedsSuccess : BedsEvents()
    data object ResetBedsFailure : BedsEvents()

    data class PerDayPriceChanged(val newValue: String) : BedsEvents()
    data class AvailabilityChanged(val newValue: String) : BedsEvents()
    data class AvailableUnitsChanged(val newValue: String) : BedsEvents()
}

fun BedsStates.isAddBedFormValid(): Boolean {
    return perDayBedPriceINR.isNotBlank() && availability.isNotBlank() &&
            availableUnits.isNotBlank() &&
            perDayBedPriceINRError == null && availabilityError == null &&
            availableUnitsError == null
}