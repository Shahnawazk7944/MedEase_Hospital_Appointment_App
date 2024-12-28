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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.domain.model.Bed
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import com.example.medeaseclient.presentation.features.common.LoadingDialog
import com.example.medeaseclient.presentation.features.common.getSnackbarToastMessage
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.BedsEvents
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.BedsStates
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.BedsViewModel

@Composable
fun BedsScreen(
    viewModel: BedsViewModel = hiltViewModel(),
    hospitalId: String,
    navController: NavHostController
) {
    LaunchedEffect(Unit) { viewModel.bedsEvents(BedsEvents.GetBeds) }
    LaunchedEffect(Unit) { viewModel.bedsEvents(BedsEvents.GetBedsFromHospital(hospitalId)) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = state.bedsSuccess) {
        if (state.bedsSuccess != null) {
            val successMessage = getSnackbarToastMessage(state.bedsSuccess)
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            viewModel.bedsEvents(BedsEvents.ResetBedsSuccess)
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

    BedsScreenContent(
        hospitalId = hospitalId,
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = { navController.navigateUp() },
        onAddNewBedClick = { bed ->
            viewModel.bedsEvents(
                BedsEvents.AddBed(
                    hospitalId = hospitalId,
                    bed = bed
                )
            )
        },
        onUpdateBedClick = { oldDoctor ->
//            navController.navigate(
//                ClientRoutes.AddDoctorScreen(
//                    doctor = oldDoctor,
//                    hospitalId = hospitalId
//                )
//            )
        },
        onBedDelete = { bedId ->
            viewModel.bedsEvents(
                BedsEvents.DeleteBed(
                    hospitalId = hospitalId,
                    bedId = bedId
                )
            )
        },
    )
}


@Composable
fun BedsScreenContent(
    hospitalId: String,
    state: BedsStates,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onUpdateBedClick: (bed: Bed) -> Unit,
    onBedDelete: (bedId: String) -> Unit,
    onAddNewBedClick: (bed: Bed) -> Unit,
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "Beds",
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
        LoadingDialog(state.loading)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.mediumLarge),
        ) {
            item(key = "available_beds") {
            Text(
                text = "Available Beds",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
            item(key = "common_beds") {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(bottom = MaterialTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
                ) {
                    items(
                        state.beds.filter { bed ->
                            !state.bedsFromHospital.any { it.bedId == bed.bedId }
                        },

                        key = { it.bedId }
                    ) { bed ->
                        Card(
                            modifier = Modifier
                                .width(300.dp)
                                .wrapContentHeight()
                                .padding(vertical = MaterialTheme.spacing.medium),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(MaterialTheme.spacing.medium)
                            ) {

                                Text(
                                    text = bed.bedType,
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
                                    text = bed.purpose,
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
                                bed.features.forEach { feature ->
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
                                        text = "Price/Day: ₹${bed.perDayBedPriceINR}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "Available: ${bed.availableUnits}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                                // Add Bed Button
                                PrimaryButton(
                                    onClick = { onAddNewBedClick.invoke(bed) },
                                    shape = MaterialTheme.shapes.medium,
                                    label = "Add Bed",
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    enabled = true
                                )
                            }
                        }
                    }
                }
            }
            if (state.bedsFromHospital.isNotEmpty()) {
                item(key = "added_beds") {
                    Text(
                        text = "Added Beds",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            items(
                state.bedsFromHospital,
                key = { it.bedId }
            ) { bed ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.medium)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(MaterialTheme.spacing.medium)
                    ) {
                        // Header Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = bed.bedType,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { onUpdateBedClick.invoke(bed) }) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = "Edit Bed",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            IconButton(onClick = { onBedDelete.invoke(bed.bedId) }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteForever,
                                    contentDescription = "Delete Bed",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Text(
                            text = "Purpose:",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = bed.purpose,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        // Features
                        Text(
                            text = "Features:",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        bed.features.forEach { feature ->
                            Text(
                                text = "• $feature",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
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
                                text = "Price/Day: ₹${bed.perDayBedPriceINR}",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Available: ${bed.availableUnits}",
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

        }
    }
}

@PreviewLightDark
@Composable
fun BedsScreenContentPreview() {
    val beds = listOf(
        Bed(
            bedId = "bed1",
            bedType = "ICU",
            purpose = "Emergency",
            features = listOf("Ventilator", "Oxygen"),
            perDayBedPriceINR = "5000",
            availableUnits = "3"
        ),
        Bed(
            bedId = "bed2",
            bedType = "General Ward",
            purpose = "Routine Care",
            features = listOf("Bedside Table", "TV", "Bedside Table", "TV"),
            perDayBedPriceINR = "1500",
            availableUnits = "5"
        ),
        Bed(
            bedId = "bed3",
            bedType = "Semi-Private",
            purpose = "Post-Surgery",
            features = listOf("Shared Bathroom", "Refrigerator", "Shared Bathroom", "Refrigerator"),
            perDayBedPriceINR = "2500",
            availableUnits = "2"
        )
    )
    MedEaseTheme {
        BedsScreenContent(
            hospitalId = "hospitalId",
            state = BedsStates(
                bedsFromHospital = beds.take(2),
                beds = beds
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = { /*TODO*/ },
            onUpdateBedClick = { /*TODO*/ },
            onBedDelete = { /*TODO*/ },
            onAddNewBedClick = { /*TODO*/ }
        )
    }
}