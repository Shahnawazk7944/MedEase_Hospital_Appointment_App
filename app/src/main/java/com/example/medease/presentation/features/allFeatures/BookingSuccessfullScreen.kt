package com.example.medease.presentation.features.allFeatures

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medease.R
import com.example.medease.domain.model.AppointmentDetails
import com.example.medease.domain.model.Bed
import com.example.medease.domain.model.Booking
import com.example.medease.domain.model.Doctor
import com.example.medease.domain.model.HospitalWithDoctors
import com.example.medease.domain.model.PaymentDetails
import com.example.medease.presentation.features.allFeatures.viewModels.PaymentAndBookingEvents
import com.example.medease.presentation.features.allFeatures.viewModels.PaymentAndBookingStates
import com.example.medease.presentation.features.allFeatures.viewModels.PaymentAndBookingViewModel
import com.example.medease.presentation.features.common.CustomTopBar
import com.example.medease.presentation.features.common.LoadingDialog
import com.example.medease.presentation.features.common.QrCodeImage
import com.example.medease.presentation.features.common.getSnackbarMessage
import java.util.Locale

@Composable
fun BookingSuccessScreen(
    viewModel: PaymentAndBookingViewModel = hiltViewModel(),
    appointmentId: String,
    transactionId: String,
    userId: String,
    navController: NavHostController,
) {
    LaunchedEffect(Unit) {
        viewModel.paymentAndBookingEvents(
            PaymentAndBookingEvents.FetchBookingDetails(
                appointmentId = appointmentId,
                transactionId = transactionId,
                userId = userId
            )
        )
    }

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
    BookingSuccessScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = { navController.navigateUp() },
    )
}

@Composable
fun BookingSuccessScreenContent(
    state: PaymentAndBookingStates,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "",
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
        if (state.loading) {
            LoadingDialog(true)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(MaterialTheme.spacing.large)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Icon and Title
                Icon(
                    imageVector = Icons.Default.Verified, // Use your icon resource
                    contentDescription = "Success",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                Text(
                    "Booking Successful!",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Your booking is confirmed.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                // Booking Summary Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Column(modifier = Modifier.padding(MaterialTheme.spacing.large)) {
                        Text(
                            text = "Booking Details",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (state.booking.appointmentDetails.status == "Pending confirmation") {
                                Image(
                                    painter = painterResource(id = R.drawable.blur_qr),
                                    contentDescription = "Blur QR Code",
                                    modifier = Modifier
                                        .size(150.dp)
                                )
                            } else {
                                if (state.booking.appointmentDetails.appointmentId.isNotBlank()) {
                                    QrCodeImage(
                                        content = "${state.booking.appointmentDetails.appointmentId}-${state.booking.appointmentDetails.bookingDate}-${state.booking.appointmentDetails.bookingTime}",
                                        size = 150.dp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                        // Hospital Details
                        Row {
                            Text(
                                text = "Hospital : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.appointmentDetails.hospital.hospitalName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "City : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = "${state.booking.appointmentDetails.hospital.hospitalCity} - ${state.booking.appointmentDetails.hospital.hospitalPinCode}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Phone : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.appointmentDetails.hospital.hospitalPhone,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                        // Doctor Details
                        Row {
                            Text(
                                text = "Doctor : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = "${state.booking.appointmentDetails.doctor.name} (${state.booking.appointmentDetails.doctor.specialist})",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Experience : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.appointmentDetails.doctor.experience,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Fees (${
                                    state.booking.appointmentDetails.bookingQuota.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    }
                                }) : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            val doctorFee = when (state.booking.appointmentDetails.bookingQuota) {
                                "general" -> state.booking.appointmentDetails.doctor.generalFees
                                "care" -> state.booking.appointmentDetails.doctor.careFees
                                "emergency" -> state.booking.appointmentDetails.doctor.emergencyFees
                                else -> "No fees found"
                            }
                            Text(
                                text = "₹$doctorFee",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }

                        Row {
                            Text(
                                text = "Symptoms Treated : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.appointmentDetails.doctor.treatedSymptoms,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                        // Appointment Details
                        Row {
                            Text(
                                text = "Date : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.appointmentDetails.bookingDate,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Time : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.appointmentDetails.bookingTime,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Quota : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.appointmentDetails.bookingQuota.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                },
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                        // Bed Details (Optional)
                        state.booking.appointmentDetails.bed?.let { bed ->
                            Row {
                                Text(
                                    text = "Bed Type : ",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = bed.bedType,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                            Row {
                                Text(
                                    text = "Purpose : ",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = bed.purpose,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                            Row {
                                Text(
                                    text = "Features : ",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = bed.features.joinToString(),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                            Row {
                                Text(
                                    text = "Price Per Day : ",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = "₹${bed.perDayBedPriceINR}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                // Payment Summary Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Column(modifier = Modifier.padding(MaterialTheme.spacing.large)) {
                        Text(
                            "Payment Details", style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row {
                            Text(
                                text = "Appointment ID : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.paymentDetails.appointmentId,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Transaction ID : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.paymentDetails.transactionId,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Date : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.paymentDetails.date,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Payment Type : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.paymentDetails.paymentType,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Amount Paid : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.paymentDetails.amountPaid,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Admin Charges : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = "₹${state.booking.paymentDetails.adminCharges}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "Status : ",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = state.booking.paymentDetails.status,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(
                    onClick = { onBackClick.invoke() },
                    shape = RoundedCornerShape(MaterialTheme.spacing.large),
                    label = "Back to Home",
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun BookingSuccessScreenPreview() {
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
    val paymentDetails = PaymentDetails(
        appointmentId = "TXN1234567890",
        transactionId = "TXN1234567890",
        date = "2023-12-31",
        paymentType = "Credit Card",
        amountPaid = "5000",
        adminCharges = "0",
        status = "Success"
    )
    MedEaseTheme {
        BookingSuccessScreenContent(
            state = PaymentAndBookingStates(booking = Booking(appointmentDetails, paymentDetails)),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {}
        )
    }
}