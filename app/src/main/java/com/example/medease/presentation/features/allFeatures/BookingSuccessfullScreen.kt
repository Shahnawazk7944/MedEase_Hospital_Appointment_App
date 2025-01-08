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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medease.domain.model.AppointmentDetails
import com.example.medease.domain.model.Bed
import com.example.medease.domain.model.Doctor
import com.example.medease.domain.model.HospitalWithDoctors
import com.example.medease.domain.model.PaymentDetails
import com.example.medease.presentation.features.common.CustomTopBar
import java.util.Locale

@Composable
fun BookingSuccessScreen(
    appointmentDetails: AppointmentDetails,
    paymentDetails: PaymentDetails,
    navController: NavHostController,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { navController.navigateUp() },
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

                    // Hospital Details
                    Row {
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
                    Row {
                        Text(
                            text = "City : ",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "${appointmentDetails.hospital.hospitalCity} - ${appointmentDetails.hospital.hospitalPinCode}",
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
                            text = appointmentDetails.hospital.hospitalPhone,
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
                            text = "${appointmentDetails.doctor.name} (${appointmentDetails.doctor.specialist})",
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
                            text = appointmentDetails.doctor.experience,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    Row {
                        Text(
                            text = "Fees (${
                                appointmentDetails.bookingQuota.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                }
                            }) : ",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = when (appointmentDetails.bookingQuota) {
                                "general" -> appointmentDetails.doctor.generalFees
                                "care" -> appointmentDetails.doctor.careFees
                                "emergency" -> appointmentDetails.doctor.emergencyFees
                                else -> "No fees found"
                            },
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
                            text = appointmentDetails.doctor.treatedSymptoms,
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
                            text = appointmentDetails.bookingDate,
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
                            text = appointmentDetails.bookingTime,
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
                            text = appointmentDetails.bookingQuota.replaceFirstChar {
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
                    appointmentDetails.bed?.let { bed ->
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
                            text = paymentDetails.transactionId,
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
                            text = paymentDetails.transactionId,
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
                            text = paymentDetails.date,
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
                            text = paymentDetails.paymentType,
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
                            text = "₹${paymentDetails.amountPaid}",
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
                            text = "₹${paymentDetails.adminCharges}",
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
                            text = paymentDetails.status,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                onClick = { navController.navigateUp() },
                shape = RoundedCornerShape(MaterialTheme.spacing.large),
                label = "Back to Home",
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    appointmentDetails: AppointmentDetails,
    navController: NavHostController,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    // Define a bottom sheet state with the default value as Expanded
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
    )
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
                        selected = true,
                        onClick = { },
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
                        selected = false,
                        onClick = { },
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
                    text = "Total: ₹${appointmentDetails.totalPrice}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(
                    onClick = { /* Confirm booking logic */ },
                    shape = RoundedCornerShape(MaterialTheme.spacing.large),
                    label = "Pay Now",
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
        border = BorderStroke(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
            width = 1.dp)
    ) {

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().height(50.dp),
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
                modifier = Modifier.fillMaxWidth().height(50.dp),
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
                modifier = Modifier.fillMaxWidth().height(50.dp),
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
                modifier = Modifier.fillMaxWidth().height(50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total : ",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "₹${appointmentDetails.totalPrice}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
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
        BookingSuccessScreen(
            appointmentDetails, paymentDetails, navController = rememberNavController(),
        )
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
            appointmentDetails,
            navController = rememberNavController(),
        )
    }
}
