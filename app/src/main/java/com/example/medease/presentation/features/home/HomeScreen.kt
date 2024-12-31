package com.example.medease.presentation.features.home


import Routes
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.medease.domain.model.UserProfile
import com.example.medease.presentation.features.common.CustomTopBar
import com.example.medease.presentation.features.common.HomeHeadings
import com.example.medease.presentation.features.common.LoadingDialog
import com.example.medease.presentation.features.common.getSnackbarMessage
import com.example.medease.presentation.features.home.viewmodels.HomeEvents
import com.example.medease.presentation.features.home.viewmodels.HomeStates
import com.example.medease.presentation.features.home.viewmodels.HomeViewModel

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
        }
    )
}

@Composable
fun HomeContent(
    state: HomeStates,
    snackbarHostState: SnackbarHostState,
    event: (HomeEvents) -> Unit,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onUserHomeOptionClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                },
                actions = {
                    IconButton(onClick = { onLogoutClick.invoke() }) {
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
        if(state.loggingOut || state.loading) { LoadingDialog(true) }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = MaterialTheme.spacing.mediumLarge),
            horizontalAlignment = Alignment.Start,
            contentPadding = paddingValues
        ) {
            item(key = "screen_heading") {
                HomeHeadings(
                    modifier = Modifier.fillMaxWidth().padding(vertical = MaterialTheme.spacing.mediumLarge),
                    heading = "Welcome ${state.userProfile?.name.toString()}",
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
                            UserHomeOptionItem(
                                label = option.first,
                                icon = option.second,
                                onClick = { onUserHomeOptionClick.invoke(options.indexOf(option)) }
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    "Find a Doctor by Hospitals",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
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
    MedEaseTheme {
        val state = HomeStates(
            userProfile = UserProfile(
                userId = "abcdefghijklmnopqrstuvwxyz",
                name = "John Doe",
                phone = "1234567890",
                email = "john.doe@example.com",
            )
        )
        val snackbarHostState = SnackbarHostState()
        HomeContent(
            state = state,
            snackbarHostState = snackbarHostState,
            event = {},
            onBackClick = {},
            onLogoutClick = {},
            onUserHomeOptionClick = {}
        )
    }
}