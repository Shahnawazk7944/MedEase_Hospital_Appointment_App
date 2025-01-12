package com.example.medease.presentation.features.allFeatures

import Routes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medease.domain.model.PaymentDetails
import com.example.medease.presentation.features.allFeatures.viewModels.TransactionsEvents
import com.example.medease.presentation.features.allFeatures.viewModels.TransactionsStates
import com.example.medease.presentation.features.allFeatures.viewModels.TransactionsViewModel
import com.example.medease.presentation.features.common.CustomTopBar
import com.example.medease.presentation.features.common.LoadingDialog
import com.example.medease.presentation.features.common.getSnackbarMessage

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel = hiltViewModel(),
    userId: String,
    navController: NavHostController
) {
    LaunchedEffect(Unit) { viewModel.transactionsEvents(TransactionsEvents.GetTransactions(userId)) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = state.failure) {
        state.failure?.let {
            val errorMessage = getSnackbarMessage(state.failure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.transactionsEvents(TransactionsEvents.RemoveFailure)
        }
    }

    TransactionsContent(
        state = state,
        onBackClick = { navController.navigateUp() },
        snackbarHostState = snackbarHostState,
        onTransactionClick = { appointmentId, transactionId, userId ->
            navController.navigate(
                Routes.BookingSuccessScreen(
                    appointmentId = appointmentId,
                    transactionId = transactionId,
                    userId = userId
                )
            )
        }
    )
}

@Composable
fun TransactionsContent(
    state: TransactionsStates,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onTransactionClick: (appointmentId: String, transactionId: String, userId: String) -> Unit,
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "My Transactions",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "sort icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) {
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    snackbarData = it,
                    actionColor = MaterialTheme.colorScheme.secondary,
                    dismissActionContentColor = MaterialTheme.colorScheme.secondary
                )
            }
        },
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.loading) {
                item { LoadingDialog(true) }
            }
            if (state.transactions.isNotEmpty()) {
                items(state.transactions, key = { it.transactionId }) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        onTransactionClick = onTransactionClick
                    )
                }
            } else if (!state.loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Transactions Found",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun TransactionCard(
    transaction: PaymentDetails,
    onTransactionClick: (appointmentId: String, transactionId: String, userId: String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.spacing.small)
            .clickable {
                onTransactionClick.invoke(
                    transaction.appointmentId,
                    transaction.transactionId,
                    transaction.userId
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
        ),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        ) {
            // Row for Transaction ID and Date
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.small),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Transaction ID: ${transaction.transactionId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = transaction.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Divider for separation
            HorizontalDivider(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            // Appointment ID and User Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.small),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Appointment ID: ${transaction.appointmentId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Payment Type: ${transaction.paymentType}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Payment and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Amount Paid: ${transaction.amountPaid}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Admin Charges: ₹${transaction.adminCharges}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = transaction.status,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (transaction.status == "Success") Color(0xFF4CAF50) else Color(
                        0xFFFF5722
                    ),
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TransactionsContentPreview() {
    MedEaseTheme {
        val state = TransactionsStates(
            loading = false,
            failure = null,
            transactions = listOf(
                PaymentDetails(
                    transactionId = "1",
                    appointmentId = "1",
                    userId = "1",
                    amountPaid = "100",
                    paymentType = "UPI",
                    status = "Success",
                    adminCharges = "0.0",
                    date = "2023-10-26"
                ),
                PaymentDetails(
                    transactionId = "2",
                    appointmentId = "2",
                    userId = "2",
                    amountPaid = "200",
                    paymentType = "UPI",
                    status = "Success",
                    adminCharges = "0.0",
                    date = "2023-10-26"
                ),
                PaymentDetails(
                    transactionId = "3",
                    appointmentId = "3",
                    userId = "3",
                    amountPaid = "300",
                    paymentType = "UPI",
                    status = "Success",
                    adminCharges = "0.0",
                    date = "2023-10-26"
                ),
                PaymentDetails(
                    transactionId = "4",
                    appointmentId = "4",
                    userId = "4",
                    amountPaid = "400",
                    paymentType = "UPI",
                    status = "Success",
                    adminCharges = "0.0",
                    date = "2023-10-26"
                ),
                PaymentDetails(
                    transactionId = "5",
                    appointmentId = "5",
                    userId = "5",
                    amountPaid = "500",
                    paymentType = "UPI",
                    status = "Success",
                    adminCharges = "0.0",
                    date = "2023-10-26"
                ),
            )
        )
        val snackbarHostState = SnackbarHostState()

        TransactionsContent(
            state = state,
            snackbarHostState = snackbarHostState,
            onBackClick = {},
            onTransactionClick = { _, _, _ -> }
        )
    }
}