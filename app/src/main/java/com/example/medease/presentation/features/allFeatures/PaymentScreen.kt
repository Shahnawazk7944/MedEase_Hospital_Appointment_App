package com.example.medease.presentation.features.allFeatures

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
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
import androidx.navigation.compose.rememberNavController
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medease.data.repository.allFeatures.UserAllFeaturesSuccess
import com.example.medease.domain.model.AppointmentDetails
import com.example.medease.domain.model.Bed
import com.example.medease.domain.model.Doctor
import com.example.medease.domain.model.HospitalWithDoctors
import com.example.medease.domain.model.PaymentDetails
import com.example.medease.presentation.features.allFeatures.viewModels.PaymentAndBookingEvents
import com.example.medease.presentation.features.allFeatures.viewModels.PaymentAndBookingViewModel
import com.example.medease.presentation.features.common.CustomTopBar
import com.example.medease.presentation.features.common.LoadingDialog
import com.example.medease.presentation.features.common.getSnackbarMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    viewModel: PaymentAndBookingViewModel = hiltViewModel(),
    appointmentDetails: AppointmentDetails,
    navController: NavHostController,
) {
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
            viewModel.paymentAndBookingEvents(PaymentAndBookingEvents.RemoveFailure)
        }
    }
    LaunchedEffect(key1 = state.paymentSuccess) {
        state.paymentSuccess?.let {
            if (it is UserAllFeaturesSuccess.PaymentSuccessful) {
                navController.navigate(
                    Routes.BookingSuccessScreen(
                        appointmentId = it.appointmentId,
                        transactionId = it.transactionId,
                        userId = it.userId
                    )
                ) {
                    popUpTo(Routes.HomeScreen) {
                        inclusive = false
                    }
                }
            }
        }
    }

    // Define a bottom sheet state with the default value as Expanded
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
    )
    LoadingDialog(state.loading)
    BottomSheetScaffold(
        containerColor = MaterialTheme.colorScheme.background,
        scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = bottomSheetState,
            snackbarHostState = snackbarHostState
        ),
        topBar = {
            CustomTopBar(
                onBackClick = { navController.navigateUp() },
                title = {
                    Text(
                        text = "Payment",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                },
            )
        },
        sheetContainerColor = MaterialTheme.colorScheme.onBackground,
        sheetSwipeEnabled = false,
        sheetShadowElevation = 20.dp,
        sheetDragHandle = { },
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
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.large)
            ) {
                Text(
                    text = "Payment Methods",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = MaterialTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "UPI", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    RadioButton(
                        selected = state.paymentMethod == "UPI",
                        onClick = {
                            viewModel.paymentAndBookingEvents(
                                PaymentAndBookingEvents.OnPaymentMethodChange(
                                    "UPI"
                                )
                            )
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = MaterialTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Cash", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    RadioButton(
                        selected = state.paymentMethod == "Cash",
                        onClick = {
                            viewModel.paymentAndBookingEvents(
                                PaymentAndBookingEvents.OnPaymentMethodChange(
                                    "Cash"
                                )
                            )
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
                Text(
                    text = "Total: ${appointmentDetails.totalPrice}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(
                    onClick = {
                        val appointmentId = generateAppointmentId()
                        val transactionId = generateTransactionId()
                        viewModel.paymentAndBookingEvents(
                            PaymentAndBookingEvents.OnPayNowClick(
                                paymentDetails = PaymentDetails(
                                    appointmentId = appointmentId,
                                    transactionId = transactionId,
                                    userId = appointmentDetails.userId,
                                    date = SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    ).format(
                                        Date()
                                    ),
                                    paymentType = state.paymentMethod,
                                    amountPaid = appointmentDetails.totalPrice,
                                    adminCharges = "0.0",
                                    status = "Success",
                                ),
                                appointmentDetails = appointmentDetails.copy(
                                    appointmentId = appointmentId
                                )
                            )
                        )
                    },
                    shape = RoundedCornerShape(MaterialTheme.spacing.large),
                    label = if (state.paymentMethod == "Cash") "Book Now" else "Pay Now",
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Order Summary",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            )
            AppointmentDetailsCard(appointmentDetails = appointmentDetails)
        }
    }
}

fun generateAppointmentId(): String {
    val timestamp = System.currentTimeMillis()
    val randomDigits = (1000..9999).random()
    return "APT${timestamp.toString().takeLast(8)}$randomDigits"
}

fun generateTransactionId(): String {
    val timestamp = System.currentTimeMillis()
    val randomDigits = (100000..999999).random()
    return "TXN${timestamp.toString().takeLast(8)}$randomDigits"
}

@Composable
fun AppointmentDetailsCard(appointmentDetails: AppointmentDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.extraLarge),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
            width = 1.dp
        )
    ) {

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hospital : ",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = appointmentDetails.hospital.hospitalName,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.extraSmall),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                thickness = 1.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Doctor : ",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "${appointmentDetails.doctor.name} (${appointmentDetails.doctor.specialist})",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.extraSmall),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                thickness = 1.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Date : ",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = appointmentDetails.bookingDate,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Text(
                    text = "Time : ",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = appointmentDetails.bookingTime,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.extraSmall),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                thickness = 1.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total : ",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = appointmentDetails.totalPrice,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PaymentScreenPreview() {
    val hospital = HospitalWithDoctors(
        hospitalName = "City Hospital",
        hospitalCity = "New York",
        hospitalPinCode = "10001",
        hospitalPhone = "123-456-7890"
    )
    val doctor = Doctor(
        name = "Dr. John Doe",
        specialist = "Cardiologist",
        experience = "10 years",
        generalFees = "500",
        careFees = "700",
        emergencyFees = "300",
        treatedSymptoms = "Heart diseases, etc."
    )
    val bed = Bed(
        bedType = "Deluxe",
        purpose = "General Ward",
        features = listOf("AC", "TV", "Refrigerator"),
        perDayBedPriceINR = "1500"
    )
    val appointmentDetails = AppointmentDetails(
        hospital = hospital,
        doctor = doctor,
        bed = bed,
        bookingDate = "2023-12-31",
        bookingTime = "10:00 AM",
        bookingQuota = "care",
        totalPrice = "5000"
    )
    MedEaseTheme {
        PaymentScreen(
            appointmentDetails = appointmentDetails,
            navController = rememberNavController(),
        )
    }
}