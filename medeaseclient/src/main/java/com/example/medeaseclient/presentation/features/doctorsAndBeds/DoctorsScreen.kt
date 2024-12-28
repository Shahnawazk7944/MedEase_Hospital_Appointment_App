package com.example.medeaseclient.presentation.features.doctorsAndBeds


import ClientRoutes
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Update
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.domain.model.Doctor
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import com.example.medeaseclient.presentation.features.common.LoadingDialog
import com.example.medeaseclient.presentation.features.common.getSnackbarToastMessage
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsEvents
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsStates
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsViewModel

@Composable
fun DoctorsScreen(
    viewModel: DoctorsViewModel = hiltViewModel(),
    hospitalId: String,
    navController: NavHostController
) {
    LaunchedEffect(Unit) { viewModel.doctorsEvents(DoctorsEvents.GetDoctors) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = state.doctorsSuccess) {
        if (state.doctorsSuccess != null) {
            val successMessage = getSnackbarToastMessage(state.doctorsSuccess)
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            viewModel.doctorsEvents(DoctorsEvents.ResetDoctorsSuccess)
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
    DoctorsScreenContent(
        hospitalId = hospitalId,
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = { navController.navigateUp() },
        onAddNewDoctorClick = {
            navController.navigate(
                ClientRoutes.AddDoctorScreen(
                    doctor = Doctor(),
                    hospitalId = hospitalId
                )
            )
        },
        onUpdateDoctorClick = { oldDoctor ->
            navController.navigate(
                ClientRoutes.AddDoctorScreen(
                    doctor = oldDoctor,
                    hospitalId = hospitalId
                )
            )
        },
        onDoctorDelete = { doctorId -> viewModel.doctorsEvents(DoctorsEvents.DeleteDoctor(doctorId)) },
    )
}


@Composable
fun DoctorsScreenContent(
    hospitalId: String,
    state: DoctorsStates,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onUpdateDoctorClick: (doctor: Doctor) -> Unit,
    onDoctorDelete: (doctorId: String) -> Unit,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item(key = state.loading || state.fetchingDoctors) { LoadingDialog(state.loading || state.fetchingDoctors) }
            item(key = "add_doctor") {
                PrimaryButton(
                    onClick = { onAddNewDoctorClick.invoke() },
                    shape = RoundedCornerShape(MaterialTheme.spacing.large),
                    label = "Add Doctors",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(vertical = MaterialTheme.spacing.medium),
                )
            }
            items(
                state.doctors.filter { it.hospitalId == hospitalId },
                key = { it.doctorId }) { doctor ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.medium)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(MaterialTheme.spacing.medium)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = doctor.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { onUpdateDoctorClick.invoke(doctor) }) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = "Edit icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                            IconButton(onClick = { onDoctorDelete.invoke(doctor.doctorId) }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteForever,
                                    contentDescription = "Delete icon",
                                    tint = MaterialTheme.colorScheme.errorContainer,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Text(
                            text = "Specialist: ${doctor.specialist}",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyLarge,
                        )

                        Text(
                            text = "Experience: ${doctor.experience} years",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Text(
                            text = "Availability: ${doctor.availabilityFrom} - ${doctor.availabilityTo}",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AvailabilityItem(
                                "General",
                                doctor.generalAvailability,
                                MaterialTheme.colorScheme.onPrimaryContainer,
                                icon = Icons.Default.LockClock
                            )
                            AvailabilityItem(
                                "Current",
                                doctor.currentAvailability,
                                MaterialTheme.colorScheme.onTertiary,
                                icon = Icons.Default.Update
                            )
                            AvailabilityItem(
                                "Emergency",
                                doctor.emergencyAvailability,
                                MaterialTheme.colorScheme.errorContainer,
                                icon = Icons.Default.Emergency
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AvailabilityItem(label: String, value: String, color: Color, icon: ImageVector) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .width(100.dp)
            .clip(RoundedCornerShape(MaterialTheme.spacing.small)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(30.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onTertiary)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.extraSmall)
                            .fillMaxSize()
                    )
                }
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = color,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@PreviewLightDark
@Composable
fun DoctorsScreenContentPreview() {
    MedEaseTheme {
        val state = DoctorsStates(
            doctors = listOf(
                Doctor(
                    doctorId = "1",
                    name = "Dr. John Doe",
                    specialist = "Cardiologist",
                    experience = "10",
                    availabilityFrom = "01-01-2023",
                    availabilityTo = "31-12-2023",
                    generalAvailability = "80",
                    currentAvailability = "50",
                    emergencyAvailability = "20"
                ),
                Doctor(
                    doctorId = "2",
                    name = "Dr. Jane Smith",
                    specialist = "Dermatologist",
                    experience = "5",
                    availabilityFrom = "01-01-2023",
                    availabilityTo = "31-12-2023",
                    generalAvailability = "70",
                    currentAvailability = "60",
                    emergencyAvailability = "30"
                ),
                Doctor(
                    doctorId = "3",
                    name = "Dr. John Doe",
                    specialist = "Cardiologist",
                    experience = "10",
                    availabilityFrom = "01-01-2023",
                    availabilityTo = "31-12-2023",
                    generalAvailability = "80",
                    currentAvailability = "50",
                    emergencyAvailability = "20"
                ),
                Doctor(
                    doctorId = "4",
                    name = "Dr. Jane Smith",
                    specialist = "Dermatologist",
                    experience = "5",
                    availabilityFrom = "01-01-2023",
                    availabilityTo = "31-12-2023",
                    generalAvailability = "70",
                    currentAvailability = "60",
                    emergencyAvailability = "30"
                ),

                )
        )
        DoctorsScreenContent(
            hospitalId = "",
            state = state,
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onAddNewDoctorClick = {},
            onDoctorDelete = {},
            onUpdateDoctorClick = {}
        )
    }
}