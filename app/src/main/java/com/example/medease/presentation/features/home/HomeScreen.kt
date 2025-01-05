package com.example.medease.presentation.features.home


import Routes
import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medease.domain.model.Bed
import com.example.medease.domain.model.Doctor
import com.example.medease.domain.model.HospitalWithDoctors
import com.example.medease.domain.model.UserProfile
import com.example.medease.presentation.features.common.CustomTopBar
import com.example.medease.presentation.features.common.HomeHeadings
import com.example.medease.presentation.features.common.LoadingDialog
import com.example.medease.presentation.features.common.getSnackbarMessage
import com.example.medease.presentation.features.home.components.SearchBarSection
import com.example.medease.presentation.features.home.components.displaySearchResults
import com.example.medease.presentation.features.home.viewmodels.HomeEvents
import com.example.medease.presentation.features.home.viewmodels.HomeStates
import com.example.medease.presentation.features.home.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.homeState.collectAsStateWithLifecycle()
    val activity = (LocalContext.current as? Activity)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.logoutFailure) {
        state.logoutFailure?.let {
            val errorMessage = getSnackbarMessage(state.logoutFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.homeEvents(HomeEvents.RemoveFailure(null))
        }
    }
    LaunchedEffect(key1 = state.userIdFailure) {
        state.userIdFailure?.let {
            val errorMessage = getSnackbarMessage(state.userIdFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
            )
        }
    }
    LaunchedEffect(key1 = state.userProfileFailure) {
        state.userProfileFailure?.let {
            val errorMessage = getSnackbarMessage(state.userProfileFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
            )
        }
    }
    LaunchedEffect(key1 = state.userOperationsFailure) {
        state.userOperationsFailure?.let {
            val errorMessage = getSnackbarMessage(state.userOperationsFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
            )
        }
    }
    LaunchedEffect(key1 = state.authenticated) {
        if (!state.authenticated) {
            navController.navigate(Routes.SignInScreen) {
                popUpTo(Routes.SignInScreen) {
                    inclusive = true
                }
            }
        }
    }
    BackHandler {
        if (activity?.isTaskRoot == true) {
            activity.finishAndRemoveTask()
        }
    }

    HomeContent(
        state = state,
        snackbarHostState = snackbarHostState,
        event = viewModel::homeEvents,
        onBackClick = {
            if (activity?.isTaskRoot == true) {
                activity.finishAndRemoveTask()
            }
        },
        onLogoutClick = {
            viewModel.homeEvents(HomeEvents.OnLogoutClick)
        },
        onUserHomeOptionClick = {
            when (it) {
                0 -> navController.navigate(
                    Routes.MyAppointmentsScreen(
                        userId = state.userProfile?.userId ?: "No ID Found"
                    )
                )

                1 -> navController.navigate(
                    Routes.HealthRecordsScreen(
                        userId = state.userProfile?.userId ?: "No ID Found"
                    )
                )

                2 -> navController.navigate(
                    Routes.TransactionsScreen(
                        userId = state.userProfile?.userId ?: "No ID Found"
                    )
                )

                3 -> navController.navigate(
                    Routes.ProfileScreen(
                        userId = state.userProfile?.userId ?: "No ID Found"
                    )
                )
            }
        },
        onSearchQueryChange = { newQuery ->
            viewModel.homeEvents(HomeEvents.SearchQueryChange(newQuery))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeStates,
    snackbarHostState: SnackbarHostState,
    event: (HomeEvents) -> Unit,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onUserHomeOptionClick: (Int) -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = onBackClick,
                title = {
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                },
                actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "logout icon",
                            tint = MaterialTheme.colorScheme.error,
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
        val scope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember { mutableStateOf(false) }

        if (state.loggingOut || state.loading) {
            LoadingDialog(true)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.mediumLarge),
            horizontalAlignment = Alignment.Start,
            contentPadding = paddingValues
        ) {
            // Display the non-search content if the search query is empty
            if (state.searchQuery.isEmpty()) {
                item(key = "screen_heading") {
                    HomeHeadings(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.spacing.mediumLarge),
                        heading = "Welcome ${state.userProfile?.name.orEmpty()}",
                        subHeading = "Start your way to a healthy life"
                    )
                }
                val options = listOf(
                    "My Appointments" to Icons.Default.Schedule,
                    "Health Records" to Icons.Default.Book,
                    "Transactions" to Icons.Default.History,
                    "Profile" to Icons.Default.PersonOutline
                )
                item(key = "medical_options") {
                    LazyVerticalGrid(
                        modifier = Modifier.height(260.dp),
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.small)
                    ) {
                        items(items = options, key = { it.first }) { option ->
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
                                UserHomeOptionItem(
                                    label = option.first,
                                    icon = option.second,
                                    onClick = { onUserHomeOptionClick(options.indexOf(option)) }
                                )
                            }
                        }
                    }
                }
            }

            item(key = "find_doctor") {
                Text(
                    "Find a Doctor by Hospitals",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            }
            item(key = "search_bar") {
                SearchBarSection(
                    modifier = Modifier,
                    searchQuery = state.searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            }
            if (state.fetchingHospitalsWithDoctors) {
                item(key = "fetching_hospitals_doctors_loading") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(50.dp),
                            strokeWidth = 8.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                }
            }
            // display the result of the search
            displaySearchResults(
                state = state,
                onBookAppointmentClick = { hospitalWithDoctor, doctor ->
                    event.invoke(HomeEvents.fetchHospitalBeds(hospitalWithDoctor.hospitalId))
                    event.invoke(HomeEvents.OnBookAppointmentClick(hospitalWithDoctor, doctor))
                    scope.launch { sheetState.show() }
                    showBottomSheet = true
                },
                lazyListScope = this
            )
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.background,
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                BookingConformationBottomSheet(
                    hospitalWithDoctors = state.selectedHospitalWithDoctors!!,
                    doctor = state.selectedDoctor!!,
                    state = state,
                    closeBottomSheet = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BookingConformationBottomSheet(
    hospitalWithDoctors: HospitalWithDoctors,
    doctor: Doctor,
    state: HomeStates,
    closeBottomSheet: () -> Unit
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
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

                // Doctor Details -------------------------------------
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
                            text = "General: ${doctor.generalAvailability}/${doctor.currentAvailability}",
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
                            text = "Available Beds",
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
                                BedCard(bed = bed)
                            }
                        }
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp),
                            )
                        }
                    }
                }

                // Confirm Booking Button ----------------------------
                item(key = "confirm_booking_button") {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    PrimaryButton(
                        onClick = { /* Confirm booking logic */ },
                        shape = RoundedCornerShape(MaterialTheme.spacing.large),
                        label = "Confirm Booking",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
fun BedCard(bed: Bed) {
    Card(
        modifier = Modifier
            .width(300.dp) // Fixed width for alignment
            .height(230.dp) // Use the precomputed max height
            .padding(vertical = MaterialTheme.spacing.medium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
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

@Composable
fun UserHomeOptionItem(
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
fun HomeContentPreview() {
    MedEaseTheme {
        val sampleHospitals = listOf(
            HospitalWithDoctors(
                hospitalName = "City Care",
                hospitalCity = "Delhi",
                hospitalPinCode = "201308",
                hospitalPhone = "7564827018",
                doctors = listOf(
                    Doctor(
                        name = "Dr. Priya",
                        specialist = "Gynaecologist",
                        treatedSymptoms = "Severe Headaches, Seizures, Numbness in Limbs, Balance Problems, Spinal Pain",
                        experience = "5",
                        generalAvailability = "10",
                        currentAvailability = "42",
                        emergencyAvailability = "20",
                        availabilityFrom = "28-12-2024",
                        availabilityTo = "29-12-2024"
                    ),
                    Doctor(
                        name = "Dr. Jack Anderson",
                        experience = "14",
                        specialist = "Gastroenterologist",
                        availabilityFrom = "28-12-2024",
                        availabilityTo = "29-12-2024",
                        generalAvailability = "Mon-Fri",
                        currentAvailability = "Available",
                        emergencyAvailability = "No",
                        treatedSymptoms = "Abdominal pain, HEADACHE, Bloating, Nausea, Vomiting, Diarrhea"
                    ),
                    Doctor(
                        name = "Dr. Kevin Martinez",
                        experience = "10",
                        specialist = "Neurosurgeon",
                        availabilityFrom = "28-12-2024",
                        availabilityTo = "29-12-2024",
                        generalAvailability = "Mon-Fri",
                        currentAvailability = "Available",
                        emergencyAvailability = "Yes",
                        treatedSymptoms = "Severe Headaches, Seizures, Numbness in Limbs, Balance Problems, Spinal Pain"
                    ),
                    Doctor(
                        name = "Dr. Linda Garcia",
                        specialist = "Gynaecologist",
                        experience = "9",
                        availabilityFrom = "28-12-2024",
                        availabilityTo = "29-12-2024",
                        generalAvailability = "Tue-Sat",
                        currentAvailability = "Available",
                        emergencyAvailability = "No",
                        treatedSymptoms = "Irregular Periods, Irregular Bowel Movements, Pelvic Pain, Pregnancy Concerns, Menopause Symptoms, Vaginal Infections"
                    )
                )
            ),
            HospitalWithDoctors(
                hospitalName = "City Care",
                hospitalCity = "Delhi",
                hospitalPinCode = "201308",
                hospitalPhone = "7564827018",
                doctors = listOf(
                    Doctor(
                        name = "Dr. Priya",
                        specialist = "Gynaecologist",
                        treatedSymptoms = "Severe Headaches, Seizures, Numbness in Limbs, Balance Problems, Spinal Pain",
                        experience = "5",
                        generalAvailability = "10",
                        currentAvailability = "42",
                        emergencyAvailability = "20",
                        availabilityFrom = "28-12-2024",
                        availabilityTo = "29-12-2024"
                    ),
                    Doctor(
                        name = "Dr. Jack Anderson",
                        experience = "14",
                        specialist = "Gastroenterologist",
                        availabilityFrom = "28-12-2024",
                        availabilityTo = "29-12-2024",
                        generalAvailability = "Mon-Fri",
                        currentAvailability = "Available",
                        emergencyAvailability = "No",
                        treatedSymptoms = "Abdominal pain, HEADACHE, Bloating, Nausea, Vomiting, Diarrhea"
                    ),
                    Doctor(
                        name = "Dr. Kevin Martinez",
                        experience = "10",
                        specialist = "Neurosurgeon",
                        availabilityFrom = "28-12-2024",
                        availabilityTo = "29-12-2024",
                        generalAvailability = "Mon-Fri",
                        currentAvailability = "Available",
                        emergencyAvailability = "Yes",
                        treatedSymptoms = "Severe Headaches, Seizures, Numbness in Limbs, Balance Problems, Spinal Pain"
                    ),
                    Doctor(
                        name = "Dr. Linda Garcia",
                        specialist = "Gynaecologist",
                        experience = "9",
                        availabilityFrom = "28-12-2024",
                        availabilityTo = "29-12-2024",
                        generalAvailability = "Tue-Sat",
                        currentAvailability = "Available",
                        emergencyAvailability = "No",
                        treatedSymptoms = "Irregular Periods, Irregular Bowel Movements, Pelvic Pain, Pregnancy Concerns, Menopause Symptoms, Vaginal Infections"
                    )
                )
            )
        )
        val state = HomeStates(
            userProfile = UserProfile(
                userId = "abcdefghijklmnopqrstuvwxyz",
                name = "John Doe",
                phone = "1234567890",
                email = "john.doe@example.com",
            ),
            hospitalsWithDoctors = sampleHospitals,
            selectedHospitalWithDoctors = sampleHospitals.first<HospitalWithDoctors>(),
            selectedDoctor = sampleHospitals.first<HospitalWithDoctors>().doctors.first<Doctor>(),
            selectedHospitalBeds = listOf(
                Bed(
                    bedId = "BD-01",
                    bedType = "Standard",
                    purpose = "General Care",
                    features = listOf("Adjustable Bed", "Call Button", "Bedside Table"),
                    perDayBedPriceINR = "2500",
                    availability = "Available",
                    availableUnits = "5"
                ),
                Bed(
                    bedId = "BD-02",
                    bedType = "Semi-Private",
                    purpose = "Post-Surgery Recovery",
                    features = listOf(
                        "Adjustable Bed",
                        "Call Button",
                        "Bedside Table",
                        "Privacy Curtain"
                    ),
                    perDayBedPriceINR = "3500",
                    availability = "Available",
                    availableUnits = "2"
                ),
                Bed(
                    bedId = "BD-03",
                    bedType = "Private",
                    purpose = "Specialized Care",
                    features = listOf(
                        "Adjustable Bed",
                        "Call Button",
                        "Bedside Table",
                        "Private Room",
                        "TV"
                    ),
                    perDayBedPriceINR = "5000",
                    availability = "Occupied",
                    availableUnits = "0"
                ),
            )
        )
        val snackbarHostState = SnackbarHostState()
        HomeContent(
            state = state,
            snackbarHostState = snackbarHostState,
            event = {},
            onBackClick = {},
            onLogoutClick = {},
            onUserHomeOptionClick = {},
            onSearchQueryChange = {}
        )
    }
}