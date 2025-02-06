package com.example.medeaseclient.presentation.features.home


import ClientRoutes
import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.components.OutlinedDateInputField
import com.example.designsystem.components.OutlinedInputField
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.domain.model.AppointmentDetails
import com.example.medeaseclient.domain.model.Doctor
import com.example.medeaseclient.domain.model.Slot
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import com.example.medeaseclient.presentation.features.common.HomeHeadings
import com.example.medeaseclient.presentation.features.common.LoadingDialog
import com.example.medeaseclient.presentation.features.common.getSnackbarToastMessage
import com.example.medeaseclient.presentation.features.home.components.AppointmentCard
import com.example.medeaseclient.presentation.features.home.components.MedicalOptionItem
import com.example.medeaseclient.presentation.features.home.viewmodels.HomeViewModel
import com.example.medeaseclient.presentation.features.home.viewmodels.events.AppointmentBottomSheetContent
import com.example.medeaseclient.presentation.features.home.viewmodels.events.AppointmentOperationEvents
import com.example.medeaseclient.presentation.features.home.viewmodels.events.AppointmentOperationsStates
import com.example.medeaseclient.presentation.features.home.viewmodels.events.HomeEvents
import com.example.medeaseclient.presentation.features.home.viewmodels.events.HomeStates
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit,
    navController: NavHostController
) {
    val state by viewModel.homeState.collectAsStateWithLifecycle()
    val activity = (LocalContext.current as? Activity)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    BackHandler {
        if (activity?.isTaskRoot == true) {
            activity.finishAndRemoveTask()
        }
    }
    LaunchedEffect(key1 = state.logoutFailure) {
        state.logoutFailure?.let {
            val errorMessage = getSnackbarToastMessage(state.logoutFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.homeEvents(HomeEvents.RemoveFailure(null))
        }
    }
    LaunchedEffect(key1 = state.appointmentStatusFailure) {
        state.appointmentStatusFailure?.let {
            val errorMessage = getSnackbarToastMessage(state.appointmentStatusFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.appointmentOperationsEvents(AppointmentOperationEvents.ClearAppointmentStatus)
        }
    }
    LaunchedEffect(key1 = state.appointmentStatusSuccess) {
        state.appointmentStatusSuccess?.let {
            val successMessage = getSnackbarToastMessage(state.appointmentStatusSuccess)
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            viewModel.appointmentOperationsEvents(AppointmentOperationEvents.ClearAppointmentStatus)
        }
    }
    LaunchedEffect(key1 = state.clientIdFailure) {
        state.clientIdFailure?.let {
            val errorMessage = getSnackbarToastMessage(state.clientIdFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
            )
        }
    }
    LaunchedEffect(key1 = state.clientProfileFailure) {
        state.clientProfileFailure?.let {
            val errorMessage = getSnackbarToastMessage(state.clientProfileFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
            )
        }
    }

    LaunchedEffect(key1 = state.authenticated) {
        if (!state.authenticated) {
            onLogoutClick.invoke()
        }
    }

    HomeContent(
        state = state,
        snackbarHostState = snackbarHostState,
        event = viewModel::homeEvents,
        appointmentEvent = viewModel::appointmentOperationsEvents,
        onBackClick = {
            if (activity?.isTaskRoot == true) {
                activity.finishAndRemoveTask()
            }
        },
        onMedicalOptionClick = {
            when (it) {
                0 -> navController.navigate(
                    ClientRoutes.DoctorScreen(
                        hospitalId = state.clientProfile?.hospitalId
                            ?: "No ID Found"
                    )
                )

                1 -> navController.navigate(
                    ClientRoutes.AppointmentScreen(
                        hospitalId = state.clientProfile?.hospitalId ?: "No ID Found",
                    )
                )

                2 -> navController.navigate(
                    ClientRoutes.BedScreen(
                        hospitalId = state.clientProfile?.hospitalId
                            ?: "No ID Found"
                    )
                )

                3 -> navController.navigate(
                    ClientRoutes.ProfileScreen(
                        hospitalId = state.clientProfile?.hospitalId ?: "No ID Found",
                        hospitalName = state.clientProfile?.hospitalName ?: "Unknown",
                        hospitalEmail = state.clientProfile?.hospitalEmail ?: "Unknown",
                        hospitalPhone = state.clientProfile?.hospitalPhone ?: "Unknown",
                        hospitalCity = state.clientProfile?.hospitalCity ?: "Unknown",
                        hospitalPinCode = state.clientProfile?.hospitalPinCode ?: "Unknown"
                    )
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeStates,
    snackbarHostState: SnackbarHostState,
    event: (HomeEvents) -> Unit,
    appointmentEvent: (AppointmentOperationEvents) -> Unit,
    onBackClick: () -> Unit,
    onMedicalOptionClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "Hospital Home",
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
        val scope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState()
        var bottomSheetContent by remember { mutableStateOf<AppointmentBottomSheetContent?>(null) }
        val todayDate by remember {
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = simpleDateFormat.format(Date())
            derivedStateOf {
                formattedDate
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.mediumLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val options = listOf(
                "Doctor" to Icons.Default.LocalHospital,
                "Appointments" to Icons.Default.Schedule,
                "Beds" to Icons.Default.Bed,
                "Profile" to Icons.Default.MedicalInformation
            )
            if (state.loggingOut || state.loading) {
                item(key = "loading") { LoadingDialog(true) }
            }
            item(key = "heading_1") {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
                HomeHeadings(
                    heading = "Welcome to ${state.clientProfile?.hospitalName} Hospital",
                    subHeading = "Leading the way in healthcare"
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            }

            item(key = "medical_options") {
                LazyVerticalGrid(
                    modifier = Modifier
                        .height(260.dp),
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.small)
                ) {
                    items(options) { option ->
                        Card(
                            modifier = Modifier
                                .clip(RoundedCornerShape(MaterialTheme.spacing.large)),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            )
                        ) {
                            MedicalOptionItem(
                                label = option.first,
                                icon = option.second,
                                onClick = { onMedicalOptionClick.invoke(options.indexOf(option)) }
                            )
                        }
                    }
                }
            }

            item(key = "heading_2") {
                Text(
                    "Today's Appointments",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            }

            val todayAppointments =
                state.todayAppointments.sortedByDescending { it.bookingDate }
                    .filter { it.bookingDate == todayDate }

            if (todayAppointments.isNotEmpty()) {
                items(
                    items = todayAppointments,
                    key = { it.appointmentId }) { appointment ->


                    AppointmentCard(
                        appointment = appointment,
                        onConfirmClick = {
                            appointmentEvent(
                                AppointmentOperationEvents.ChangeAppointmentStatus(
                                    appointment.appointmentId,
                                    "Appointment confirmed"
                                )
                            )
                        },
                        onCancelClick = {
                            appointmentEvent(
                                AppointmentOperationEvents.ChangeAppointmentStatus(
                                    appointment.appointmentId,
                                    "Appointment cancelled"
                                )
                            )
                        },
                        onRescheduleClick = { appointmentId, appointment ->
                            appointmentEvent(
                                AppointmentOperationEvents.FetchReScheduleAppointmentDoctor(
                                    appointment.doctor.doctorId
                                )
                            )
                            bottomSheetContent =
                                AppointmentBottomSheetContent.ReScheduleAppointment(
                                    appointmentId = appointmentId,
                                    appointmentDetails = appointment,
                                    newStatus = "Appointment rescheduled"
                                )
                        },
                        onCompletedClick = {
                            bottomSheetContent = AppointmentBottomSheetContent.CompleteAppointment(
                                appointmentId = appointment.appointmentId,
                                userId = appointment.userId,
                                newStatus = "Appointment completed"
                            )
                        }
                    )
                }
            } else {
                item(key = "no_appointments") {
                    Text(
                        "No Pending Appointments",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = MaterialTheme.spacing.extraLarge)
                    )
                }
            }


        }

        if (bottomSheetContent != null) {
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.background,
                onDismissRequest = {
                    appointmentEvent(AppointmentOperationEvents.ClearReScheduledAppointment)

                    bottomSheetContent = null
                },
                sheetState = sheetState
            ) {
                when (val content = bottomSheetContent) {
                    is AppointmentBottomSheetContent.CompleteAppointment -> {
                        CompleteAppointmentBottomSheetContent(
                            state = state,
                            events = appointmentEvent,
                            onCompleteRequest = { healthRemark ->
                                appointmentEvent(
                                    AppointmentOperationEvents.CompleteAppointment(
                                        appointmentId = content.appointmentId,
                                        healthRemark = healthRemark,
                                        userId = content.userId,
                                        newStatus = content.newStatus
                                    )
                                )
                                appointmentEvent(AppointmentOperationEvents.ClearCompletedAppointment)
                                bottomSheetContent = null // Close the sheet on action

                            }
                        )
                    }

                    is AppointmentBottomSheetContent.ReScheduleAppointment -> {
                        ReScheduleAppointmentBottomSheetContent(
                            state = state,
                            appointment = content.appointmentDetails,
                            events = appointmentEvent,
                            onReScheduleRequest = { newDate, newTime, newDoctor ->
                                appointmentEvent(
                                    AppointmentOperationEvents.ReScheduleAppointment(
                                        appointmentId = content.appointmentId,
                                        newDate = newDate,
                                        newTime = newTime,
                                        newStatus = content.newStatus,
                                        newDoctor = newDoctor
                                    )
                                )
                                appointmentEvent(AppointmentOperationEvents.ClearReScheduledAppointment)
                                bottomSheetContent = null // Close the sheet on action
                            }
                        )
                    }

                    null -> TODO()
                }
            }
        }
    }
}

@Composable
fun CompleteAppointmentBottomSheetContent(
    state: AppointmentOperationsStates,
    events: (AppointmentOperationEvents) -> Unit,
    onCompleteRequest: (healthRemark: String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Complete Appointment!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.medium),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(MaterialTheme.spacing.large)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                OutlinedInputField(
                    value = state.addHealthRemark,
                    onChange = {
                        events(AppointmentOperationEvents.ChangeAddHealthRemark(it))
                    },
                    label = "Add Health Remark",
                    placeholder = {
                        Text(
                            text = "Add Health Remark or NA",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Report icon",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    error = state.addHealthRemarkError,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                PrimaryButton(
                    onClick = { onCompleteRequest.invoke(state.addHealthRemark) },
                    shape = RoundedCornerShape(MaterialTheme.spacing.large),
                    label = "Mark as Complete",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End),
                    enabled = !(state.addHealthRemark.isBlank() || state.addHealthRemarkError != null)
                )

            }
        }
    }
}

@Composable
fun ReScheduleAppointmentBottomSheetContent(
    state: AppointmentOperationsStates,
    appointment: AppointmentDetails,
    events: (AppointmentOperationEvents) -> Unit,
    onReScheduleRequest: (newDate: String, newTime: String, newDoctor: Doctor) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Re-Schedule Appointment!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.medium),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(MaterialTheme.spacing.large)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        text = "Availability - ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "${appointment.doctor.availabilityFrom} - ${appointment.doctor.availabilityTo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                OutlinedDateInputField(
                    date = state.newAppointmentDate,
                    onDateChange = {
                        events(
                            AppointmentOperationEvents.ChangeNewAppointmentDate(
                                newDate = it,
                                fromDate = appointment.doctor.availabilityFrom,
                                toDate = appointment.doctor.availabilityTo
                            )
                        )
                    },
                    label = "New Date",
                    placeholder = {
                        Text(
                            text = "20-01-2024",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Calendar icon",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    error = state.newAppointmentDateError,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                if (state.newAppointmentDate.isNotEmpty() && state.newAppointmentDateError == null) {
                    OutlinedInputField(
                        value = state.newAppointmentTime,
                        onChange = {},
                        readOnly = true,
                        label = "Time",
                        placeholder = {
                            Text(
                                text = "01:00 - 1:30",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Calendar icon",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        error = state.newAppointmentTimeError,
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    AvailableSlotsSection(
                        doctor = state.rescheduleAppointmentDoctor,
                        selectedDate = state.newAppointmentDate,
                        onSlotSelected = { selectedSlot ->
                            events(AppointmentOperationEvents.ChangeNewAppointmentTime(selectedSlot.time))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                PrimaryButton(
                    onClick = {
                        val doctor = state.rescheduleAppointmentDoctor.copy()
                        val oldDate = appointment.bookingDate
                        val oldTime = appointment.bookingTime
                        val newDate = state.newAppointmentDate
                        val newTime = state.newAppointmentTime
                        doctor.availabilitySlots[oldDate]?.let { slots ->
                            val updatedSlots = slots.map {
                                if (it.time == oldTime) it.copy(available = true) else it
                            }
                            doctor.availabilitySlots = doctor.availabilitySlots.toMutableMap().apply {
                                put(oldDate, updatedSlots)
                            }
                        }

                        doctor.availabilitySlots[newDate]?.let { slots ->
                            val updatedSlots = slots.map {
                                if (it.time == newTime) it.copy(available = false) else it
                            }
                            doctor.availabilitySlots = doctor.availabilitySlots.toMutableMap().apply {
                                put(newDate, updatedSlots)
                            }
                        }

                        Log.d(
                            "----old",
                            "${doctor.availabilitySlots[oldDate]?.find { it.time == oldTime }?.available}"
                        )
                        Log.d(
                            "----new",
                            "${doctor.availabilitySlots[newDate]?.find { it.time == newTime }?.available}"
                        )

                        onReScheduleRequest.invoke(
                            state.newAppointmentDate,
                            state.newAppointmentTime,
                            doctor
                        )
                    },
                    shape = RoundedCornerShape(MaterialTheme.spacing.large),
                    label = "Re-Schedule Appointment",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End),
                    enabled = !(state.newAppointmentDateError != null || state.newAppointmentTimeError != null ||
                            state.newAppointmentDate.isBlank() || state.newAppointmentTime.isBlank())
                )


            }
        }
    }
}

@Composable
fun AvailableSlotsSection(
    doctor: Doctor,
    selectedDate: String,
    onSlotSelected: (Slot) -> Unit
) {
    val availableSlots = remember(selectedDate) {
        doctor.availabilitySlots[selectedDate] ?: emptyList<Slot>()
    }

    Column {
        if (availableSlots.isEmpty()) {
            Text(
                text = "No slots available for the selected day",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            val rows = availableSlots.chunked(16)

            rows.forEach { rowSlots ->
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(rowSlots) { slot ->
                        SlotItem(
                            slot = slot,
                            onClick = { onSlotSelected(slot) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SlotItem(slot: Slot, onClick: () -> Unit) {
    val isDisabled = !slot.available

    Box(
        modifier = Modifier
            .width(100.dp)
            .height(40.dp)
            .background(
                if (isDisabled) Color.Gray else MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isDisabled) Color.Gray else MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = !isDisabled) { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = slot.time,
            style = MaterialTheme.typography.bodySmall,
            color = if (isDisabled) Color.White else MaterialTheme.colorScheme.primary
        )
    }
}

@PreviewLightDark
@Composable
fun HomeContentPreview() {
    val state = HomeStates()
    val snackbarHostState = SnackbarHostState()
    val event: (HomeEvents) -> Unit = {}
    val appointmentEvent: (AppointmentOperationEvents) -> Unit = {}
    val onBackClick: () -> Unit = {}
    MedEaseTheme {
        HomeContent(
            state = state,
            snackbarHostState = snackbarHostState,
            event = event,
            onBackClick = onBackClick,
            onMedicalOptionClick = { },
            appointmentEvent = appointmentEvent
        )
    }
}
