package com.example.medeaseclient.presentation.features.doctorsAndBeds


import ClientRoutes
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DataExploration
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.designsystem.components.OutlinedDateInputField
import com.example.designsystem.components.OutlinedInputField
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.data.util.Validator
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@Composable
fun DoctorsScreen(
    viewModel: DoctorsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DoctorsScreenContent(
        state = state,
        onBackClick = {},
        onAddNewDoctorClick = { navController.navigate(ClientRoutes.AddDoctorScreen) },
    )
}


@Composable
fun DoctorsScreenContent(
    state: DoctorsStates,
    onBackClick: () -> Unit,
    onAddNewDoctorClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "Doctors",
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
                .padding(horizontal = MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PrimaryButton(
                onClick = { onAddNewDoctorClick.invoke() },
                shape = MaterialTheme.shapes.large,
                label = "Add Doctors",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }
    }
}

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
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier.weight(1f),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Calendar icon",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier.weight(1f),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Calendar icon",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    )
                }


        }
    }
}

data class DoctorsStates(
    val loading: Boolean = false,
    val doctorName: String = "",
    val specialist: String = "",
    val experience: String = "",
    val from: String = "",
    val to: String = "",
    val genAvail: String = "",
    val currAvail: String = "",
    val emergency: String = "",
    val doctorNameError: String? = null,
    val specialistError: String? = null,
    val experienceError: String? = null,
    val fromError: String? = null,
    val toError: String? = null,
    val genAvailError: String? = null,
    val currAvailError: String? = null,
    val emergencyError: String? = null,
)

sealed class DoctorsEvents {
    data object GetDoctors : DoctorsEvents()
    data class DoctorNameChanged(val newValue: String) : DoctorsEvents()
    data class SpecialistChanged(val newValue: String) : DoctorsEvents()
    data class ExperienceChanged(val newValue: String) : DoctorsEvents()
    data class FromChanged(val newValue: String) : DoctorsEvents()
    data class ToChanged(val newValue: String) : DoctorsEvents()
    data class GenAvailChanged(val newValue: String) : DoctorsEvents()
    data class CurrAvailChanged(val newValue: String) : DoctorsEvents()
    data class EmergencyChanged(val newValue: String) : DoctorsEvents()
}

@HiltViewModel
class DoctorsViewModel @Inject constructor(private val validator: Validator) : ViewModel() {

    private val _state = MutableStateFlow(DoctorsStates())
    val state = _state.asStateFlow()


    fun doctorsEvents(event: DoctorsEvents) {
        when (event) {
            DoctorsEvents.GetDoctors -> {

            }

            is DoctorsEvents.CurrAvailChanged -> {
                val error = validator.validateCurrAvail(event.newValue)
                _state.update {
                    it.copy(
                        currAvail = event.newValue,
                        currAvailError = error?.message
                    )
                }
            }

            is DoctorsEvents.DoctorNameChanged -> {
                val error = validator.validateDoctorName(event.newValue)
                _state.update {
                    it.copy(
                        doctorName = event.newValue,
                        doctorNameError = error?.message
                    )
                }
            }

            is DoctorsEvents.EmergencyChanged -> {
                val error = validator.validateEmergency(event.newValue)
                _state.update {
                    it.copy(
                        emergency = event.newValue,
                        emergencyError = error?.message
                    )
                }
            }

            is DoctorsEvents.ExperienceChanged -> {
                val error = validator.validateExperience(event.newValue)
                _state.update {
                    it.copy(
                        experience = event.newValue,
                        experienceError = error?.message
                    )
                }
            }

            is DoctorsEvents.FromChanged -> {
                val fromError = validator.validateFrom(
                    fromDate = event.newValue,
                    toDate = state.value.to
                )
                val toError = validator.validateTo(
                    fromDate = event.newValue,
                    toDate = state.value.to,
                )
                _state.update { currentState ->
                    currentState.copy(
                        from = event.newValue,
                        fromError = fromError?.message,
                        to = state.value.to,
                        toError = toError?.message,
                    )
                }
            }

            is DoctorsEvents.GenAvailChanged -> {
                val error = validator.validateGenAvail(event.newValue)
                _state.update {
                    it.copy(
                        genAvail = event.newValue,
                        genAvailError = error?.message
                    )
                }
            }

            is DoctorsEvents.SpecialistChanged -> {
                val error = validator.validateSpecialist(event.newValue)
                _state.update {
                    it.copy(
                        specialist = event.newValue,
                        specialistError = error?.message
                    )
                }
            }

            is DoctorsEvents.ToChanged -> {
                val toError = validator.validateTo(
                    toDate = event.newValue,
                    fromDate = state.value.from
                )
                val fromError = validator.validateFrom(
                    toDate = event.newValue,
                    fromDate = state.value.from
                )
                _state.update { currentState ->
                    currentState.copy(
                        to = event.newValue,
                        toError = toError?.message,
                        from = state.value.from,
                        fromError = fromError?.message
                    )
                }
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