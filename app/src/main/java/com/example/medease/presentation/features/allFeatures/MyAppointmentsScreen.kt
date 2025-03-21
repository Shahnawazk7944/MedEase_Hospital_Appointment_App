package com.example.medease.presentation.features.allFeatures

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.components.SecondaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medease.R
import com.example.medease.domain.model.AppointmentDetails
import com.example.medease.domain.model.Bed
import com.example.medease.domain.model.Doctor
import com.example.medease.domain.model.HospitalWithDoctors
import com.example.medease.presentation.features.allFeatures.viewModels.MyAppointmentsEvents
import com.example.medease.presentation.features.allFeatures.viewModels.MyAppointmentsStates
import com.example.medease.presentation.features.allFeatures.viewModels.MyAppointmentsViewModel
import com.example.medease.presentation.features.common.CustomTopBar
import com.example.medease.presentation.features.common.LoadingDialog
import com.example.medease.presentation.features.common.getSnackbarMessage
import com.example.medease.presentation.features.common.rememberQrBitmap

@Composable
fun MyAppointmentsScreen(
    viewModel: MyAppointmentsViewModel = hiltViewModel(),
    userId: String,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.myAppointmentsEvents(
            MyAppointmentsEvents.GetMyAppointments(
                userId
            )
        )
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = state.failure) {
        state.failure?.let {
            val errorMessage = getSnackbarMessage(state.failure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.myAppointmentsEvents(MyAppointmentsEvents.RemoveFailureAndSuccess)
        }
    }
    LaunchedEffect(key1 = state.appointmentStatusSuccess) {
        state.appointmentStatusSuccess?.let {
            val successMessage = getSnackbarMessage(state.appointmentStatusSuccess)
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
           viewModel.myAppointmentsEvents(MyAppointmentsEvents.RemoveFailureAndSuccess)
        }
    }

    MyAppointmentsContent(
        state = state,
        onBackClick = { navController.navigateUp() },
        snackbarHostState = snackbarHostState,
        events = viewModel::myAppointmentsEvents,
        onAppointmentDetailsClick = {appointmentId ->
            navController.navigate(Routes.AppointmentDetailsScreen(appointmentId))
        }
    )
}


@Composable
fun MyAppointmentsContent(
    state: MyAppointmentsStates,
    events: (MyAppointmentsEvents) -> Unit,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onAppointmentDetailsClick: (appointmentId: String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "My Appointments",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "sort icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(25.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            containerColor = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(MaterialTheme.spacing.medium),
                            offset = DpOffset((-4).dp, 4.dp),
                            tonalElevation = 2.dp,
                            shadowElevation = 6.dp
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "All Appointments",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (state.appointmentsSortBy == "All Appointments") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    events(MyAppointmentsEvents.SortAppointmentsBy("All Appointments"))
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AllInbox,
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = "reschedule"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Pending Appointments",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (state.appointmentsSortBy == "Pending Appointments") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    events(MyAppointmentsEvents.SortAppointmentsBy("Pending Appointments"))
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.PendingActions,
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = "reschedule"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Confirmed Appointments",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (state.appointmentsSortBy == "Confirmed Appointments") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    events(MyAppointmentsEvents.SortAppointmentsBy("Confirmed Appointments"))

                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = "reschedule"
                                    )
                                }
                            )


                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Re-Scheduled Appointments",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (state.appointmentsSortBy == "Re-Scheduled Appointments") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    events(MyAppointmentsEvents.SortAppointmentsBy("Re-Scheduled Appointments"))

                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = "reschedule"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Cancelled Appointments",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (state.appointmentsSortBy == "Cancelled Appointments") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    events(MyAppointmentsEvents.SortAppointmentsBy("Cancelled Appointments"))
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        tint = MaterialTheme.colorScheme.errorContainer,
                                        contentDescription = "cancel icon"
                                    )
                                }
                            )
                        }
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
            if (state.appointments.isNotEmpty()) {
                val sortedAppointments = when (state.appointmentsSortBy) {
                    "All Appointments" -> state.appointments.sortedByDescending { it.bookingDate }
                    "Pending Appointments" -> state.appointments.filter { it.status == "Pending confirmation" }
                    "Confirmed Appointments" -> state.appointments.filter { it.status == "Appointment confirmed" }
                    "Re-Scheduled Appointments" -> state.appointments.filter { it.status == "Appointment rescheduled" }
                    "Cancelled Appointments" -> state.appointments.filter { it.status == "Appointment cancelled" }
                    else -> state.appointments.sortedByDescending { it.bookingDate }
                }
                if (sortedAppointments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Appointments found according to selected sorting",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                items(sortedAppointments, key = { it.appointmentId }) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        onCancelAppointmentClick = {events(MyAppointmentsEvents.ChangeAppointmentStatus(appointment.appointmentId, "Appointment cancelled"))},
                        onAppointmentDetailsClick = {onAppointmentDetailsClick.invoke(appointment.appointmentId)}
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

@Composable
fun AppointmentCard(
    appointment: AppointmentDetails,
    onCancelAppointmentClick: () -> Unit,
    onAppointmentDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.spacing.small)
            .clickable { onAppointmentDetailsClick.invoke() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
        ),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.large),
        ) {
            // Appointment ID and Status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.small),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Appointment ID: ${appointment.appointmentId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = MaterialTheme.spacing.small)
                )
                Text(
                    text = appointment.status,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (appointment.status == "Pending confirmation" || appointment.status == "Appointment cancelled")  Color(0xFFFF5722) else Color(0xFF4CAF50),
                    textAlign = TextAlign.Center
                )
            }

            // Divider for separation
            HorizontalDivider(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.small),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Hospital: ${appointment.hospital.hospitalName}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "City: ${appointment.hospital.hospitalCity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Contact: ${appointment.hospital.hospitalPhone}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    Text(
                        text = "Doctor: ${appointment.doctor.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Specialist: ${appointment.doctor.specialist}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Experience: ${appointment.doctor.experience} years",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // QR Code
                if (appointment.status == "Pending confirmation") {
                    Image(
                        painter = painterResource(id = R.drawable.blur_qr),
                        contentDescription = "Blur QR Code",
                        modifier = Modifier
                            .size(100.dp)
                    )
                } else if(appointment.status != "Appointment cancelled") {
                    val qrCodeContent =
                        "${appointment.appointmentId}-${appointment.bookingDate}-${appointment.bookingTime}"
                    val qrCodeBitmap = rememberQrBitmap(content = qrCodeContent, size = 150.dp)

                    qrCodeBitmap?.let { bitmap ->
                        Image(
                            painter = remember(bitmap) { BitmapPainter(bitmap.asImageBitmap()) },
                            contentDescription = "QR Code",
                            modifier = Modifier
                                .size(100.dp)
                        )
                    }
                }
            }

            // Bed Information (optional)
            appointment.bed?.let { bed ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.small)
                ) {
                    Text(
                        text = "Bed Type: ${bed.bedType}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Purpose: ${bed.purpose}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Booking Date, Time, and Price
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.small),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Booking Date: ${appointment.bookingDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Time: ${appointment.bookingTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "Total Price: ${appointment.totalPrice}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            if (appointment.status != "Appointment cancelled") {
                SecondaryButton(
                    onClick = {onCancelAppointmentClick.invoke()},
                    shape = MaterialTheme.shapes.medium,
                    label = "Cancel Appointment",
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor =  MaterialTheme.colorScheme.errorContainer),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.errorContainer
                    )
                )
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
                    status = "Confirmed",
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
                        hospitalName = "Fortis Hospital",
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
            onBackClick = {},
            events = {},
            onAppointmentDetailsClick = {  }
        )
    }
}