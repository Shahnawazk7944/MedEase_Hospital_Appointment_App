package com.example.medease.presentation.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.spacing
import com.example.medease.domain.model.Doctor
import com.example.medease.domain.model.HospitalWithDoctors
import com.example.medease.presentation.features.home.viewmodels.HomeStates

/**
 * Main function to display search results
 */
fun displaySearchResults(
    state: HomeStates,
    onBookAppointmentClick: (HospitalWithDoctors, Doctor) -> Unit, // Pass doctor
    lazyListScope: LazyListScope
) {
    val filteredHospitals = state.hospitalsWithDoctors.filterBySearchQuery(state.searchQuery)


    if (filteredHospitals.isEmpty() && state.searchQuery.isNotBlank()) {
        lazyListScope.item(key = "no_search_results_matched") {
            Text(
                text = "We couldn't find any matches.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    } else if (state.searchQuery.isNotBlank()) {
        lazyListScope.item(key = "search_results") {
            Text(
                text = "Search Results",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
    lazyListScope.hospitalDoctorCards(
        filteredHospitals = filteredHospitals,
        onBookAppointmentClick = { hospitalWithDoctors, doctor ->
            // Trigger the onBookAppointmentClick lambda function
            onBookAppointmentClick(hospitalWithDoctors, doctor)
        })
}

fun List<HospitalWithDoctors>.filterBySearchQuery(searchQuery: String): List<HospitalWithDoctors> {
    if (searchQuery.isBlank()) {
        return this
    }

    return this.mapNotNull { hospitalWithDoctors ->
        val matchingDoctors = hospitalWithDoctors.doctors.filter { doctor ->
            doctor.name.contains(searchQuery, ignoreCase = true) ||
                    doctor.specialist.contains(searchQuery, ignoreCase = true) ||
                    doctor.treatedSymptoms.contains(searchQuery, ignoreCase = true)
        }

        // Check if the hospital or any of its doctors match the search query
        if (hospitalWithDoctors.hospitalName.contains(searchQuery, ignoreCase = true) ||
            hospitalWithDoctors.hospitalCity.contains(searchQuery, ignoreCase = true) ||
            hospitalWithDoctors.hospitalPinCode.contains(searchQuery, ignoreCase = true) ||
            matchingDoctors.isNotEmpty()
        ) {
            hospitalWithDoctors.copy(doctors = if (matchingDoctors.isNotEmpty()) matchingDoctors else hospitalWithDoctors.doctors)
        } else {
            // If the hospital and its doctors don't match, filter it out
            null
        }
    }
}

/**
 * Extension function to add hospital and doctor cards into the lazy list
 */
fun LazyListScope.hospitalDoctorCards(
    filteredHospitals: List<HospitalWithDoctors>,
    onBookAppointmentClick: (HospitalWithDoctors, Doctor) -> Unit, // Pass doctor
) {
    items(filteredHospitals, key = { "hospital_id_${it.hospitalId}_${it.hospitalName}" }) { hospitalWithDoctors ->
        // Display HospitalDoctorCard for each doctor in the hospital
        for (doctor in hospitalWithDoctors.doctors) {
            HospitalDoctorCard(
                hospitalName = hospitalWithDoctors.hospitalName,
                hospitalCity = hospitalWithDoctors.hospitalCity,
                hospitalPinCode = hospitalWithDoctors.hospitalPinCode,
                hospitalPhone = hospitalWithDoctors.hospitalPhone,
                doctor = doctor,
                onBookAppointmentClick = {
                    // Pass both hospital and doctor details to the callback
                    onBookAppointmentClick(hospitalWithDoctors, doctor)
                }
            )
        }
    }
}

@Composable
fun HospitalDoctorCard(
    hospitalName: String,
    hospitalCity: String,
    hospitalPinCode: String,
    hospitalPhone: String,
    doctor: Doctor,
    onBookAppointmentClick: () -> Unit
) {
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
        Column(modifier = Modifier.padding(16.dp)) {
            // Hospital Details
            Text(
                text = "$hospitalName, $hospitalCity - $hospitalPinCode",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Phone: $hospitalPhone",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Doctor Details
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

            Text(
                text = "Treated Symptoms: ${doctor.treatedSymptoms}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))


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
            Spacer(modifier = Modifier.height(12.dp))

            // Book Appointment Button
            PrimaryButton(
                onClick = onBookAppointmentClick,
                shape = RoundedCornerShape(MaterialTheme.spacing.large),
                label = "Book Appointment",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End)
            )
        }
    }
}