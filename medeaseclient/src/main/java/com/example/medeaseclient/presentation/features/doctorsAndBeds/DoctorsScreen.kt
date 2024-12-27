package com.example.medeaseclient.presentation.features.doctorsAndBeds


import ClientRoutes
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsStates
import com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.DoctorsViewModel

@Composable
fun DoctorsScreen(
    viewModel: DoctorsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DoctorsScreenContent(
        state = state,
        onBackClick = { navController.navigateUp() },
        onAddNewDoctorClick = { navController.navigate(ClientRoutes.AddDoctorScreen) },
    )
}


@Composable
fun DoctorsScreenContent(
    state: DoctorsStates,
    onBackClick: () -> Unit,
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
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
            items(state.doctors) { doctor ->
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
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = "Edit icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                            IconButton(onClick = { }) {
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
        DoctorsScreenContent(state, {}, {})
    }
}