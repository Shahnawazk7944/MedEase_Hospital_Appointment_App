package com.example.medease.presentation.features.allFeatures.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class TransactionsStates(
    val loading: Boolean = false,
)

sealed class TransactionsEvents {
    data object GetTransactions : TransactionsEvents()
}

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    // private val clientHomeRepository: ClientHomeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsStates())
    val state = _state.asStateFlow()


    fun transactionsEvents(event: TransactionsEvents) {
        when (event) {
            TransactionsEvents.GetTransactions -> {}
        }
    }
}