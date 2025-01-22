package com.example.medease.presentation.features.allFeatures.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medease.data.repository.allFeatures.UserAllFeaturesFailure
import com.example.medease.data.repository.allFeatures.UserAllFeaturesRepository
import com.example.medease.domain.model.PaymentDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class TransactionsStates(
    val loading: Boolean = false,
    val transactionsSortBy: String = "All Transactions",
    val failure: UserAllFeaturesFailure? = null,
    val transactions: List<PaymentDetails> = emptyList()
)

sealed class TransactionsEvents {
    data class GetTransactions(val userId: String) : TransactionsEvents()
    data object RemoveFailure : TransactionsEvents()
    data class SortTransactionsBy(val query: String) : TransactionsEvents()
}

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: UserAllFeaturesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsStates())
    val state = _state.asStateFlow()


    fun transactionsEvents(event: TransactionsEvents) {
        when (event) {
            is TransactionsEvents.GetTransactions -> {
                fetchMyTransactions(event.userId)
            }

            TransactionsEvents.RemoveFailure -> {
                _state.update { it.copy(failure = null) }
            }

            is TransactionsEvents.SortTransactionsBy -> {
                _state.update { it.copy(transactionsSortBy = event.query) }
            }
        }
    }

    private fun fetchMyTransactions(userId: String) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchMyTransactions(userId = userId).onRight { myTransactions ->
                _state.update { it.copy(transactions = myTransactions, loading = false) }
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