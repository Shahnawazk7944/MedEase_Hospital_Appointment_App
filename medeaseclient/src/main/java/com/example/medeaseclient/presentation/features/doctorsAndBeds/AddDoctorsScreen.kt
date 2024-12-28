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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DataExploration
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Update
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
import androidx.navigation.compose.rememberNavController
import com.example.designsystem.components.OutlinedDateInputField
import com.example.designsystem.components.OutlinedInputField
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.data.repository.doctor.DoctorsSuccess
import com.example.medeaseclient.domain.model.Doctor
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import com.example.medeaseclient.presentation.features.common.LoadingDialog
import com.example.medeaseclient.presentation.features.common.getSnackbarToastMessage
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsEvents
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsStates
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsViewModel
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.isAddDoctorFormValid

@Composable
fun AddDoctorsScreen(
    viewModel: DoctorsViewModel = hiltViewModel(),
    doctor: Doctor,
    hospitalId: String,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.doctorsEvents(
            DoctorsEvents.FillDoctorForm(
                doctor.copy(
                    hospitalId = hospitalId
                )
            )
        )
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = state.doctorsSuccess) {
        if (state.doctorsSuccess != null) {
            val successMessage = getSnackbarToastMessage(state.doctorsSuccess)
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            viewModel.doctorsEvents(DoctorsEvents.ResetDoctorsSuccess)
            if(DoctorsSuccess.DoctorUpdated == state.doctorsSuccess){
                navController.navigateUp()
            }
        }
    }
    LaunchedEffect(key1 = state.doctorsFailure) {
        if (state.doctorsFailure != null) {
            val errorMessage = getSnackbarToastMessage(state.doctorsFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.doctorsEvents(DoctorsEvents.ResetDoctorsFailure)
        }
    }

    AddDoctorsScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = { navController.navigateUp() },
        onAddNewDoctorClick = {
            viewModel.doctorsEvents(
                DoctorsEvents.AddDoctor(
                    Doctor(
                        doctorId = "",
                        hospitalId = state.hospitalId,
                        name = state.doctorName,
                        specialist = state.specialist,
                        experience = state.experience,
                        availabilityFrom = state.from,
                        availabilityTo = state.to,
                        generalAvailability = state.genAvail,
                        currentAvailability = state.currAvail,
                        emergencyAvailability = state.emergency
                    )
                )
            )
        },
        onUpdateDoctorClick = { viewModel.doctorsEvents(
            DoctorsEvents.UpdateDoctor(
                Doctor(
                    doctorId = state.doctorId,
                    hospitalId = state.hospitalId,
                    name = state.doctorName,
                    specialist = state.specialist,
                    experience = state.experience,
                    availabilityFrom = state.from,
                    availabilityTo = state.to,
                    generalAvailability = state.genAvail,
                    currentAvailability = state.currAvail,
                    emergencyAvailability = state.emergency
                )
            )
        ) },
        events = viewModel::doctorsEvents
    )
}

@Composable
fun AddDoctorsScreenContent(
    state: DoctorsStates,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onAddNewDoctorClick: () -> Unit,
    onUpdateDoctorClick: () -> Unit,
    events: (DoctorsEvents) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = if (state.doctorId.isNotBlank()) "Update Doctor" else "Add Doctor",
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
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            OutlinedInputField(
                value = state.doctorName,
                onChange = {
                    events(DoctorsEvents.DoctorNameChanged(it))
                },
                label = "Doctor Name",
                placeholder = {
                    Text(
                        text = "Dr. Alex Smith",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                error = state.doctorNameError,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            OutlinedInputField(
                value = state.specialist,
                onChange = {
                    events(DoctorsEvents.SpecialistChanged(it))
                },
                label = "Specialist",
                placeholder = {
                    Text(
                        text = "Neurologist",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Specialist icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                error = state.specialistError,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            OutlinedInputField(
                value = state.experience,
                onChange = {
                    events(DoctorsEvents.ExperienceChanged(it))
                },
                label = "Experience",
                placeholder = {
                    Text(
                        text = "5",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DataExploration,
                        contentDescription = "Number icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                error = state.experienceError,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.small),
            ) {
                Text(
                    "Availability",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedDateInputField(
                        date = state.from,
                        onDateChange = {
                            events(DoctorsEvents.FromChanged(it))
                        },
                        label = "From",
                        placeholder = {
                            Text(
                                text = "20-01-2024",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar icon",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        error = state.fromError,
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    OutlinedDateInputField(
                        date = state.to,
                        onDateChange = {
                            events(DoctorsEvents.ToChanged(it))
                        },
                        label = "To",
                        placeholder = {
                            Text(
                                text = "30-01-2024",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar icon",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        error = state.toError,
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedInputField(
                        value = state.genAvail,
                        onChange = {
                            events(DoctorsEvents.GenAvailChanged(it))
                        },
                        label = "General Availability",
                        placeholder = {
                            Text(
                                text = "80",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LockClock,
                                contentDescription = "Number icon",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        error = state.genAvailError,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    OutlinedInputField(
                        value = state.currAvail,
                        onChange = {
                            events(DoctorsEvents.CurrAvailChanged(it))
                        },
                        label = "Current Availability",
                        placeholder = {
                            Text(
                                text = "50",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Update,
                                contentDescription = "Number icon",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        error = state.currAvailError,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    OutlinedInputField(
                        value = state.emergency,
                        onChange = {
                            events(DoctorsEvents.EmergencyChanged(it))
                        },
                        label = "Emergency",
                        placeholder = {
                            Text(
                                text = "10",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Emergency,
                                contentDescription = "Number icon",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        error = state.emergencyError,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
                PrimaryButton(
                    onClick = { if (state.doctorId.isNotBlank()) onUpdateDoctorClick.invoke() else onAddNewDoctorClick.invoke() },
                    shape = MaterialTheme.shapes.large,
                    label = if (state.doctorId.isNotBlank()) "Update Doctor" else "Add Doctor",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isAddDoctorFormValid()
                )
            }

        }
    }
}

@PreviewLightDark
@Composable
fun AddDoctorsScreenPreview() {
    val navController = rememberNavController()
    MedEaseTheme {
        AddDoctorsScreenContent(
            state = DoctorsStates(),
            onBackClick = {},
            onAddNewDoctorClick = {},
            events = {},
            onUpdateDoctorClick = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}