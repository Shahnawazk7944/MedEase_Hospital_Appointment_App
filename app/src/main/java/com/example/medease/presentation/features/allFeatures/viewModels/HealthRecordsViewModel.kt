package com.example.medease.presentation.features.allFeatures.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medease.data.repository.allFeatures.UserAllFeaturesFailure
import com.example.medease.data.repository.allFeatures.UserAllFeaturesRepository
import com.example.medease.domain.model.HealthRecord
import com.example.medease.domain.model.PaymentDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HealthRecordsStates(
    val loading: Boolean = false,
    val failure: UserAllFeaturesFailure? = null,
    val healthRecords: List<HealthRecord> = emptyList()
)

sealed class HealthRecordsEvents {
    data class GetHealthRecords(val userId: String) : HealthRecordsEvents()
}

@HiltViewModel
class HealthRecordsViewModel @Inject constructor(
    private val repository: UserAllFeaturesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HealthRecordsStates())
    val state = _state.asStateFlow()


    fun healthRecordsEvents(event: HealthRecordsEvents) {
        when (event) {
           is HealthRecordsEvents.GetHealthRecords -> {
                fetchMyHealthRecords(event.userId)
            }
        }
    }
    private fun fetchMyHealthRecords(userId: String) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchMyHealthRecords(userId = userId).onRight { myHealthRecords ->
                _state.update { it.copy(healthRecords = myHealthRecords, loading = false) }
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