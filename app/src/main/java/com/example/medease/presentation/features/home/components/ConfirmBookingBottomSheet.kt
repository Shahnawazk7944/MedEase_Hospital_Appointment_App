package com.example.medease.presentation.features.home.components

import android.R.attr.onClick
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.OutlinedDateInputField
import com.example.designsystem.components.OutlinedInputField
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.spacing
import com.example.medease.domain.model.AppointmentDetails
import com.example.medease.domain.model.Bed
import com.example.medease.domain.model.Doctor
import com.example.medease.domain.model.HospitalWithDoctors
import com.example.medease.domain.model.Slot
import com.example.medease.presentation.features.home.viewmodels.HomeEvents
import com.example.medease.presentation.features.home.viewmodels.HomeStates
import com.example.medease.presentation.features.home.viewmodels.isConfirmBookingFormValid
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun BookingConformationBottomSheet(
    hospitalWithDoctors: HospitalWithDoctors,
    userId: String,
    doctor: Doctor,
    state: HomeStates,
    events: (HomeEvents) -> Unit,
    onConfirmAppointmentClick: (AppointmentDetails) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Booking Confirmation!",
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
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                // Hospital Details ---------------------------------
                item(key = "hospital_details_1") {
                    Text(
                        text = "${hospitalWithDoctors.hospitalName}, ${hospitalWithDoctors.hospitalCity} - ${hospitalWithDoctors.hospitalPinCode}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item(key = "hospital_details_2") {
                    Text(
                        text = "Phone: ${hospitalWithDoctors.hospitalPhone}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }
                item(key = "hospital_details_3") {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }

                // Doctor Details ------------------------------------
                item(key = "doctor_details_1") {
                    Text(
                        text = "${doctor.name} (Exp: ${doctor.experience}yrs)",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = doctor.specialist,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }
                item(key = "doctor_details_2") {
                    Text(
                        text = "Treated Symptoms: ${doctor.treatedSymptoms}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }

                // Availability -------------------------------------
                item(key = "doctor_details_3") {
                    // Availability
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Text(
                            text = "Quota - ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "General: ${doctor.generalAvailability}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Care: ${doctor.careAvailability}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Emergency: ${doctor.emergencyAvailability}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.errorContainer
                        )
                    }
                    //Price
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Text(
                            text = "Fees - ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "General: ₹${doctor.generalFees}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Care: ₹${doctor.careFees}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Emergency: ₹${doctor.emergencyFees}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.errorContainer
                        )
                    }
                }
                item(key = "doctor_details_4") {
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
                            text = "${doctor.availabilityFrom} - ${doctor.availabilityTo}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                item(key = "hospital_details_4.1") {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                item(key = "doctor_details_5") {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    OutlinedDateInputField(
                        date = state.bookingDate,
                        onDateChange = {
                            events(
                                HomeEvents.BookingDateChange(
                                    newDate = it,
                                    fromDate = doctor.availabilityFrom,
                                    toDate = doctor.availabilityTo
                                )
                            )
                        },
                        label = "Date",
                        placeholder = {
                            Text(
                                text = "20-01-2024",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        modifier = Modifier,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar icon",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        error = state.bookingDateError,
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    if (state.bookingDate.isNotEmpty() && state.bookingDateError == null) {
                        OutlinedInputField(
                            value = state.bookingTime,
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
                            error = state.bookingTimeError,
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                        AvailableSlotsSection(
                            doctor = doctor,
                            selectedDate = state.bookingDate,
                            onSlotSelected = { selectedSlot ->
                                events(HomeEvents.BookingTimeChange(selectedSlot.time))
                            }
                        )
                    }
                }
                // Booking Quota Selection
                item(key = "booking_quota_selection") {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    Text(
                        text = "Select Booking Quota:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RadioButton(
                            enabled = doctor.generalAvailability.toInt() > 0,
                            selected = state.selectedQuota == "general",
                            onClick = { events(HomeEvents.BookingQuotaChange("general")) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text("General")
                        RadioButton(
                            enabled = doctor.careAvailability.toInt() > 0,
                            selected = state.selectedQuota == "care",
                            onClick = { events(HomeEvents.BookingQuotaChange("care")) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.tertiary
                            )
                        )
                        Text("Care")
                        RadioButton(
                            enabled = doctor.emergencyAvailability.toInt() > 0,
                            selected = state.selectedQuota == "emergency",
                            onClick = { events(HomeEvents.BookingQuotaChange("emergency")) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.errorContainer
                            )
                        )
                        Text("Emergency")
                    }
                }

                // Beds ---------------------------------------------
                item(key = "hospital_details_4") {
                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }


                // LazyRow for Beds ---------------------------------
                item(key = "hospital_beds") {
                    Spacer(modifier = Modifier.height(6.dp))
                    if (!state.fetchingHospitalsBeds) {
                        Text(
                            text = if (state.selectedHospitalBeds.isNotEmpty()) "Available Beds" else "No Beds Available",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.small),
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                            verticalAlignment = Alignment.Top
                        ) {
                            items(
                                items = state.selectedHospitalBeds,
                                key = { it.bedId }
                            ) { bed ->
                                BedCard(
                                    bed = bed,
                                    isSelected = state.selectedBed?.bedId == bed.bedId, // Check if this bed is selected
                                    onSelect = { selectedBed ->
                                        events(
                                            HomeEvents.OnSelectBedClick(
                                                selectedBed
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }

                // Confirm Booking Button ----------------------------
                item(key = "confirm_booking_button") {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                    // Calculate total price
                    val doctorFees = when (state.selectedQuota) {
                        "general" -> doctor.generalFees.toDoubleOrNull() ?: 0.0
                        "care" -> doctor.careFees.toDoubleOrNull() ?: 0.0
                        "emergency" -> doctor.emergencyFees.toDoubleOrNull() ?: 0.0
                        else -> 0.0
                    }
                    val bedPrice = state.selectedBed?.perDayBedPriceINR?.toDoubleOrNull() ?: 0.0
                    val totalPrice = doctorFees + bedPrice

                    // Format total price with rupee symbol
                    val formattedTotalPrice =
                        NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(totalPrice)


                    PrimaryButton(
                        onClick = {
                            onConfirmAppointmentClick.invoke(
                                AppointmentDetails(
                                    appointmentId = "",
                                    hospital = hospitalWithDoctors.copy(doctors = emptyList()),
                                    userId = userId,
                                    hospitalId = hospitalWithDoctors.hospitalId,
                                    doctor = doctor,
                                    bed = state.selectedBed,
                                    bookingDate = state.bookingDate,
                                    bookingTime = state.bookingTime,
                                    bookingQuota = state.selectedQuota,
                                    totalPrice = formattedTotalPrice,
                                    status = "Pending confirmation" // or "Pending confirmation" or similar
                                )
                            )
                        },
                        shape = RoundedCornerShape(MaterialTheme.spacing.large),
                        label = "Confirm Booking $formattedTotalPrice",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.End),
                        enabled = state.isConfirmBookingFormValid()
                    )
                }
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


@Composable
fun BedCard(
    bed: Bed,
    isSelected: Boolean, // Whether the bed is selected
    onSelect: (Bed?) -> Unit // Callback to handle selection
) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(230.dp)
            .padding(vertical = MaterialTheme.spacing.medium)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box {
            // Checkbox for selection
            Checkbox(
                checked = isSelected,
                onCheckedChange = {
                    if (isSelected) {
                        // If already selected, deselect it
                        onSelect(null) // Pass an empty Bed to deselect
                    } else {
                        // Otherwise, select it
                        onSelect(bed)
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(MaterialTheme.spacing.small)
            )

            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            ) {
                Text(
                    text = bed.bedType,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Text(
                    text = "Purpose:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = bed.purpose,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Text(
                    text = "Features:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                bed.features.forEach { feature ->
                    Text(
                        text = "• $feature",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = MaterialTheme.spacing.small)
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Price/Day: ₹${bed.perDayBedPriceINR}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "${bed.availability} : ${bed.availableUnits}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
