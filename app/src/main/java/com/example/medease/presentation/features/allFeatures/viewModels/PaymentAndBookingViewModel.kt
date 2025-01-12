package com.example.medease.presentation.features.allFeatures.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medease.data.repository.allFeatures.UserAllFeaturesFailure
import com.example.medease.data.repository.allFeatures.UserAllFeaturesRepository
import com.example.medease.data.repository.allFeatures.UserAllFeaturesSuccess
import com.example.medease.domain.model.AppointmentDetails
import com.example.medease.domain.model.Booking
import com.example.medease.domain.model.PaymentDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class PaymentAndBookingStates(
    val loading: Boolean = false,
    val paymentMethod: String = "UPI",
    val failure: UserAllFeaturesFailure? = null,
    val paymentSuccess: UserAllFeaturesSuccess? = null,
    val booking: Booking = Booking()
)

sealed class PaymentAndBookingEvents {
    data class OnPaymentMethodChange(val paymentMethod: String) : PaymentAndBookingEvents()
    data class OnPayNowClick(
        val paymentDetails: PaymentDetails,
        val appointmentDetails: AppointmentDetails
    ) : PaymentAndBookingEvents()

    data class FetchBookingDetails(
        val appointmentId: String,
        val userId: String,
        val transactionId: String
    ) : PaymentAndBookingEvents()

    data object RemoveFailure : PaymentAndBookingEvents()
}

@HiltViewModel
class PaymentAndBookingViewModel @Inject constructor(
    private val repository: UserAllFeaturesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PaymentAndBookingStates())
    val state = _state.asStateFlow()


    fun paymentAndBookingEvents(event: PaymentAndBookingEvents) {
        when (event) {
            is PaymentAndBookingEvents.OnPaymentMethodChange -> {
                _state.update { it.copy(paymentMethod = event.paymentMethod) }
            }

            is PaymentAndBookingEvents.OnPayNowClick -> {
                createAppointment(event.paymentDetails, event.appointmentDetails)
            }

            is PaymentAndBookingEvents.FetchBookingDetails -> {
                fetchBookingDetails(event.appointmentId, event.userId, event.transactionId)
            }

            PaymentAndBookingEvents.RemoveFailure -> {
                _state.update { it.copy(failure = null) }
            }
        }
    }

    private fun createAppointment(
        paymentDetails: PaymentDetails,
        appointmentDetails: AppointmentDetails
    ) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.createAppointment(
                paymentDetails = paymentDetails,
                appointmentDetails = appointmentDetails
            ).onRight { paymentSuccess ->
                delay(1000)
                _state.update { it.copy(paymentSuccess = paymentSuccess, loading = false) }
            }.onLeft { failure ->
                _state.update {
                    it.copy(
                        loading = false,
                        failure = failure
                    )
                }
            }
        }
    }

    private fun fetchBookingDetails(appointmentId: String, userId: String, transactionId: String) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchBookingDetails(
                appointmentId = appointmentId,
                userId = userId,
                transactionId = transactionId
            ).onRight { booking ->
                _state.update { it.copy(booking = booking, loading = false) }
            }.onLeft { failure ->
                _state.update {
                    it.copy(
                        loading = false,
                        failure = failure
                    )
                }
            }
        }
    }
}



