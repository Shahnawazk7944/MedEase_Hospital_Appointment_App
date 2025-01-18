package com.example.medeaseclient.presentation.features.allFeatures

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.domain.model.AppointmentDetails
import com.example.medeaseclient.domain.model.Bed
import com.example.medeaseclient.domain.model.Doctor
import com.example.medeaseclient.domain.model.HospitalWithDoctors
import com.example.medeaseclient.presentation.features.allFeatures.viewModels.MyAppointmentsEvents
import com.example.medeaseclient.presentation.features.allFeatures.viewModels.MyAppointmentsStates
import com.example.medeaseclient.presentation.features.allFeatures.viewModels.MyAppointmentsViewModel
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import com.example.medeaseclient.presentation.features.common.LoadingDialog
import com.example.medeaseclient.presentation.features.common.getSnackbarToastMessage
import com.example.medeaseclient.presentation.features.home.components.AppointmentCard

@Composable
fun MyAppointmentsScreen(
    viewModel: MyAppointmentsViewModel = hiltViewModel(),
    hospitalId: String,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.myAppointmentsEvents(
            MyAppointmentsEvents.GetMyAppointments(
                hospitalId
            )
        )
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.failure) {
        state.failure?.let {
            val errorMessage = getSnackbarToastMessage(state.failure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.myAppointmentsEvents(MyAppointmentsEvents.RemoveFailure)
        }
    }

    MyAppointmentsContent(
        state = state,
        onBackClick = { navController.navigateUp() },
        snackbarHostState = snackbarHostState
    )
}


@Composable
fun MyAppointmentsContent(
    state: MyAppointmentsStates,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "Appointments",
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
                .fillMaxSize().padding(horizontal = MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.loading) {
                item { LoadingDialog(true) }
            }
            if (state.appointments.isNotEmpty()) {
                items(state.appointments, key = { it.appointmentId }) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        onConfirmClick = {},
                        onCancelClick = {},
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
                            text = "No Appointments Found",
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

@PreviewLightDark
@Composable
fun MyAppointmentsContentPreview() {
    MedEaseTheme {
        val state = MyAppointmentsStates(
            loading = false,
            appointments = listOf(
                AppointmentDetails(
                    appointmentId = "1",
                    hospital = HospitalWithDoctors(
                        hospitalName = "City Hospital",
                        hospitalCity = "New Delhi",
                        hospitalPhone = "1234567890",
                    ),
                    doctor = Doctor(
                        name = "Dr. John Doe",
                        specialist = "Cardiologist",
                        experience = "10 years"
                    ),
                    bed = Bed(
                        bedType = "Regular",
                        purpose = "General"
                    ),
                    bookingDate = "2023-12-15",
                    bookingTime = "10:00 AM",
                    status = "Appointment Completed",
                    totalPrice = "1000",
                ),
                AppointmentDetails(
                    appointmentId = "2",
                    hospital = HospitalWithDoctors(
                        hospitalName = "General Hospital",
                        hospitalCity = "Mumbai",
                        hospitalPhone = "9876543210",
                    ),
                    doctor = Doctor(
                        name = "Dr. Jane Smith",
                        specialist = "Neurologist",
                        experience = "15 years"
                    ),
                    bed = Bed(
                        bedType = "ICU",
                        purpose = "Critical Care"
                    ),
                    bookingDate = "2023-12-20",
                    bookingTime = "02:00 PM",
                    status = "Pending",
                    totalPrice = "1500",
                ),
                AppointmentDetails(
                    appointmentId = "3",
                    hospital = HospitalWithDoctors(
                        hospitalName = "Apollo Hospital",
                        hospitalCity = "Bangalore",
                        hospitalPhone = "5555555555",
                    ),
                    doctor = Doctor(
                        name = "Dr. David Wilson",
                        specialist = "Orthopedic",
                        experience = "8 years"
                    ),
                    bed = null, // No bed needed
                    bookingDate = "2023-12-22",
                    bookingTime = "11:30 AM",
                    status = "Confirmed",
                    totalPrice = "800",
                ),
                AppointmentDetails(
                    appointmentId = "4",
                    hospital = HospitalWithDoctors(
                        hospitalName = "Max Hospital",
                        hospitalCity = "Chennai",
                        hospitalPhone = "7777777777",
                    ),
                    doctor = Doctor(
                        name = "Dr. Emily Brown",
                        specialist = "Dermatologist",
                        experience = "12 years"
                    ),
                    bed = Bed(
                        bedType = "Semi-Private",
                        purpose = "Post-Surgery"
                    ),
                    bookingDate = "2023-12-28",
                    bookingTime = "04:00 PM",
                    status = "Pending",
                    totalPrice = "1200",
                ),
                AppointmentDetails(
                    appointmentId = "5",
                    hospital = HospitalWithDoctors(
                        hospitalName = "Forties Hospital",
                        hospitalCity = "Kolkata",
                        hospitalPhone = "8888888888",
                    ),
                    doctor = Doctor(
                        name = "Dr. Michael Davis",
                        specialist = "Gastroenterologist",
                        experience = "10 years"
                    ),
                    bed = Bed(
                        bedType = "Deluxe",
                        purpose = "Long-Term Care"
                    ),
                    bookingDate = "2024-01-05",
                    bookingTime = "09:00 AM",
                    status = "Confirmed",
                    totalPrice = "2000",
                ),
            )
        )
        val snackbarHostState = SnackbarHostState()

        MyAppointmentsContent(
            state = state,
            snackbarHostState = snackbarHostState,
            onBackClick = {}
        )
    }
}