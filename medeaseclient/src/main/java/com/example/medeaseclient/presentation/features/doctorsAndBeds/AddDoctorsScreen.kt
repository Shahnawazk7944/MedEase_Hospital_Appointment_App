package com.example.medeaseclient.presentation.features.doctorsAndBeds

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsEvents
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsStates
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsViewModel
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.isAddDoctorFormValid

@Composable
fun AddDoctorsScreen(
    viewModel: DoctorsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AddDoctorsScreenContent(
        state = state,
        onBackClick = { navController.navigateUp() },
        onAddNewDoctorClick = { navController.navigate(ClientRoutes.AddDoctorScreen) },
        events = viewModel::doctorsEvents
    )

}

@Composable
fun AddDoctorsScreenContent(
    state: DoctorsStates,
    onBackClick: () -> Unit,
    onAddNewDoctorClick: () -> Unit,
    events: (DoctorsEvents) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "Add Doctors",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.mediumLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
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
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
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
                    onClick = {},
                    shape = MaterialTheme.shapes.large,
                    label = "Add Doctor",
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
            events = {}
        )
    }
}