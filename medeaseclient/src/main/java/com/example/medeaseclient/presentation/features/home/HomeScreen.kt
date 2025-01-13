package com.example.medeaseclient.presentation.features.home


import ClientRoutes
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import com.example.medeaseclient.presentation.features.common.HomeHeadings
import com.example.medeaseclient.presentation.features.common.LoadingDialog
import com.example.medeaseclient.presentation.features.common.getSnackbarToastMessage
import com.example.medeaseclient.presentation.features.home.viewmodels.HomeViewModel
import com.example.medeaseclient.presentation.features.home.viewmodels.events.HomeEvents
import com.example.medeaseclient.presentation.features.home.viewmodels.events.HomeStates

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit,
    navController: NavHostController
) {
    val state by viewModel.homeState.collectAsStateWithLifecycle()
    val activity = (LocalContext.current as? Activity)
    val snackbarHostState = remember { SnackbarHostState() }

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
        onBackClick = {
            if (activity?.isTaskRoot == true) {
                activity.finishAndRemoveTask()
            }
        },
        onMedicalOptionClick = {
            when (it) {
                0 -> navController.navigate(ClientRoutes.DoctorScreen(hospitalId = state.clientProfile?.hospitalId
                    ?:"No ID Found"))
                1 -> navController.navigate(ClientRoutes.AppointmentScreen)
                2 -> navController.navigate(ClientRoutes.BedScreen(hospitalId = state.clientProfile?.hospitalId
                    ?:"No ID Found"))
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


@Composable
fun HomeContent(
    state: HomeStates,
    snackbarHostState: SnackbarHostState,
    event: (HomeEvents) -> Unit,
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
        Column(
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
                LoadingDialog(true)
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
            HomeHeadings(
                heading = "Welcome to ${state.clientProfile?.hospitalName} Hospital",
                subHeading = "Leading the way in healthcare"
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            LazyVerticalGrid(
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

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                item {
                    Text(
                        "Today's Appointments",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Start,
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
                }
                items(10) {
                    Text(
                        "Today's Appointments",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Start,
                    )
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
        modifier = Modifier.fillMaxSize().padding(MaterialTheme.spacing.medium),
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
    val state = HomeStates()
    val snackbarHostState = SnackbarHostState()
    val event: (HomeEvents) -> Unit = {}
    val onBackClick: () -> Unit = {}
    MedEaseTheme {
        HomeContent(
            state = state,
            snackbarHostState = snackbarHostState,
            event = event,
            onBackClick = onBackClick,
            onMedicalOptionClick = { }
        )
    }
}
