package com.example.medeaseclient.presentation.features.home.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.components.SecondaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.domain.model.AppointmentDetails
import com.example.medeaseclient.domain.model.Bed
import com.example.medeaseclient.domain.model.Doctor
import com.example.medeaseclient.domain.model.HospitalWithDoctors

@Composable
fun AppointmentCard(
    appointment: AppointmentDetails,
    onConfirmClick: (appointmentId: String) -> Unit,
    onCancelClick: (appointmentId: String) -> Unit,
    onRescheduleClick: (appointmentId: String, appointment: AppointmentDetails) -> Unit,
    onCompletedClick: (appointmentId: String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.spacing.medium),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = .2f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
        ),
        shape = RoundedCornerShape(MaterialTheme.spacing.large)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.large),
        ) {
            // Appointment ID and Status
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
                    color = if (appointment.status == "Pending confirmation" || appointment.status == "Appointment cancelled") Color(
                        0xFFFF5722
                    ) else Color(0xFF4CAF50),
                    textAlign = TextAlign.Center
                )
            }

            // Divider for separation
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.spacing.small),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                thickness = 1.dp
            )
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

            if (appointment.status == "Appointment completed" || appointment.status == "Appointment cancelled") {
                SecondaryButton(
                    onClick = {},
                    shape = MaterialTheme.shapes.medium,
                    label = if (appointment.status == "Appointment completed") "Appointment Completed" else "Appointment Cancelled",
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = if (appointment.status == "Appointment cancelled") MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primary),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (appointment.status == "Appointment cancelled") MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primary
                    )
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = MaterialTheme.spacing.small),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PrimaryButton(
                        onClick = {
                            onCompletedClick.invoke(appointment.appointmentId)
                        },
                        shape = MaterialTheme.shapes.medium,
                        label = "Mark Complete",
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 2.dp
                        ),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(0.dp)
                    )

                    Box {
                        OutlinedIconButton(
                            onClick = { expanded = true },
                            modifier = Modifier.height(40.dp),
                            colors = IconButtonDefaults.outlinedIconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = "More icon"
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
                            if (appointment.status == "Pending confirmation") {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "Confirm Appointment",
                                            style = MaterialTheme.typography.titleMedium,
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        onConfirmClick.invoke(appointment.appointmentId)
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            tint = MaterialTheme.colorScheme.primary,
                                            contentDescription = "reschedule"
                                        )
                                    }
                                )
                            }

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Re-Schedule Appointment",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    onRescheduleClick.invoke(appointment.appointmentId, appointment)

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
                                        text = "Cancel Appointment",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    onCancelClick.invoke(appointment.appointmentId)
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

            }
        }
    }
}


@Composable
fun MedicalOptionItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
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
                onClick = onClick,
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                contentColor = MaterialTheme.colorScheme.onSecondary,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.medium)
                        .fillMaxSize()
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@PreviewLightDark
@Composable
fun AppointmentCardPreview() {
    MedEaseTheme {
        val appointments = listOf(
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
        Column(modifier = Modifier.fillMaxSize()) {
            repeat(appointments.size) {
                AppointmentCard(
                    appointment = appointments[it],
                    onConfirmClick = {},
                    onCancelClick = {},
                    onRescheduleClick = { _, _ -> },
                    onCompletedClick = {}
                )
            }
        }
    }
}