package com.example.medease.presentation.features.allFeatures

import android.R.attr.bottom
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.theme.spacing
import com.example.medease.domain.model.HealthRecord
import com.example.medease.domain.model.PaymentDetails
import com.example.medease.presentation.features.allFeatures.viewModels.HealthRecordsEvents
import com.example.medease.presentation.features.allFeatures.viewModels.HealthRecordsStates
import com.example.medease.presentation.features.allFeatures.viewModels.HealthRecordsViewModel
import com.example.medease.presentation.features.allFeatures.viewModels.TransactionsEvents
import com.example.medease.presentation.features.allFeatures.viewModels.TransactionsStates
import com.example.medease.presentation.features.common.CustomTopBar
import com.example.medease.presentation.features.common.LoadingDialog
import com.example.medease.presentation.features.common.getSnackbarMessage
import kotlinx.coroutines.delay

@Composable
fun HealthRecordsScreen(
    viewModel: HealthRecordsViewModel = hiltViewModel(),
    userId: String,
    navController: NavHostController
) {
    LaunchedEffect(Unit) { viewModel.healthRecordsEvents(HealthRecordsEvents.GetHealthRecords(userId)) }
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
            delay(1000)
            navController.navigateUp()
        }
    }

    HealthRecordsContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = {navController.navigateUp()},
        onHealthRecordsClick = { appointmentId ->
            navController.navigate(Routes.AppointmentDetailsScreen(appointmentId))
        }
    )
}


@Composable
fun HealthRecordsContent(
    state: HealthRecordsStates,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onHealthRecordsClick: (appointmentId: String) -> Unit,
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "Health Records",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                },
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
            if (state.healthRecords.isNotEmpty()) {
                items(state.healthRecords, key = { it.healthRecordId }) { healthRecord ->
                    HealthRecordCard(
                        healthRecord = healthRecord,
                        onHealthRecordClick = { onHealthRecordsClick.invoke(healthRecord.appointmentId) },
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
                            text = "No Health Records Found",
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
fun HealthRecordCard(
    healthRecord: HealthRecord,
    onHealthRecordClick: (appointmentId: String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth().heightIn(min = 160.dp)
            .padding(bottom = MaterialTheme.spacing.small)
            .clickable {
                onHealthRecordClick.invoke(
                    healthRecord.appointmentId,
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
            // Row for Health Record ID
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.small),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Health Record ID: ${healthRecord.healthRecordId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
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
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Appointment ID: ${healthRecord.appointmentId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Health Remark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Column {
                    Text(
                        text = "Health Remark :",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        text = healthRecord.healthRemark,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = MaterialTheme.spacing.medium)
                    )
                }
            }
        }
    }
}
