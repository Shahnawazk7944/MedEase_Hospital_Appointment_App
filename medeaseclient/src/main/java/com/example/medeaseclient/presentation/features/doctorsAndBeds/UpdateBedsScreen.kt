package com.example.medeaseclient.presentation.features.doctorsAndBeds

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.components.OutlinedInputField
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.data.repository.doctor.DoctorsSuccess
import com.example.medeaseclient.domain.model.Bed
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import com.example.medeaseclient.presentation.features.common.LoadingDialog
import com.example.medeaseclient.presentation.features.common.getSnackbarToastMessage
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.BedsEvents
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.BedsStates
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.BedsViewModel
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.isAddBedFormValid

@Composable
fun UpdateBedsScreen(
    viewModel: BedsViewModel = hiltViewModel(),
    bed: Bed,
    hospitalId: String,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.bedsEvents(
            BedsEvents.FillBedsForm(
                bed = bed
            )
        )
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = state.bedsSuccess) {
        if (state.bedsSuccess != null) {
            val successMessage = getSnackbarToastMessage(state.bedsSuccess)
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            if (DoctorsSuccess.BedUpdated == state.bedsSuccess) {
                navController.navigateUp()
                viewModel.bedsEvents(BedsEvents.ResetBedsSuccess)
            }
        }
    }
    LaunchedEffect(key1 = state.bedsFailure) {
        if (state.bedsFailure != null) {
            val errorMessage = getSnackbarToastMessage(state.bedsFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.bedsEvents(BedsEvents.ResetBedsFailure)
        }
    }

    UpdateBedsScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = { navController.navigateUp() },
        onUpdateBedClick = {
            viewModel.bedsEvents(
                BedsEvents.UpdateBed(
                    hospitalId = hospitalId,
                    bed = Bed(
                        bedId = state.bedId,
                        bedType = state.bedType,
                        purpose = state.purpose,
                        features = state.features,
                        perDayBedPriceINR = state.perDayBedPriceINR,
                        availability = state.availability,
                        availableUnits = state.availableUnits,
                    )
                )
            )
        },
        events = viewModel::bedsEvents
    )
}

@Composable
fun UpdateBedsScreenContent(
    state: BedsStates,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onUpdateBedClick: () -> Unit,
    events: (BedsEvents) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "Update Bed",
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.mediumLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.loading) {
                LoadingDialog(true)
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                ) {
                    Text(
                        text = state.bedType,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    // Purpose
                    Text(
                        text = "Purpose:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = state.purpose,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    // Features
                    Text(
                        text = "Features:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    state.features.forEach { feature ->
                        Text(
                            text = "• $feature",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = MaterialTheme.spacing.small)
                        )
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    // Bed Price and Availability
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Price/Day: ₹${state.perDayBedPriceINR}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "${state.availability} : ${state.availableUnits}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            Text(
                "Update Bed Details",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedInputField(
                    value = state.perDayBedPriceINR,
                    onChange = {
                        events(BedsEvents.PerDayPriceChanged(it))
                    },
                    label = "Price/Day",
                    placeholder = {
                        Text(
                            text = "4000",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Money icon",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    error = state.perDayBedPriceINRError,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                OutlinedInputField(
                    value = state.availability,
                    onChange = {
                        events(BedsEvents.AvailabilityChanged(it))
                    },
                    label = "Availability",
                    placeholder = {
                        Text(
                            text = "Available",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.EventAvailable,
                            contentDescription = "Availability icon",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    error = state.availabilityError,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                OutlinedInputField(
                    value = state.availableUnits,
                    onChange = {
                        events(BedsEvents.AvailableUnitsChanged(it))
                    },
                    label = "Available Units",
                    placeholder = {
                        Text(
                            text = "10",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.ProductionQuantityLimits,
                            contentDescription = "Availability units icon",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    error = state.availableUnitsError,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
            PrimaryButton(
                onClick = { onUpdateBedClick.invoke() },
                shape = MaterialTheme.shapes.large,
                label = "Update Bed",
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isAddBedFormValid()
            )

        }
    }
}

@PreviewLightDark
@Composable
fun UpdateBedsScreenPreview() {
    MedEaseTheme {
        UpdateBedsScreenContent(

            state = BedsStates(
                purpose = "For patients on ventilators.",
                bedId = "1",
                bedType = "ICU",
                features = listOf<String>(
                    "Air-filled compartments for pressure redistribution.",
                    "Adjustable firmness.",
                    "Suitable for immobile patients."
                ),
                perDayBedPriceINR = "1100",
                availability = "Available",
                availableUnits = "14"
            ),
            onBackClick = {},
            events = {},
            onUpdateBedClick = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}