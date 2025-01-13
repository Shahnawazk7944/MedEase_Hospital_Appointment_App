package com.example.medeaseclient.presentation.features.helper

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.components.SecondaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.medeaseclient.data.repository.home.ClientHomeRepository
import com.example.medeaseclient.data.repository.home.LogoutFailure
import com.example.medeaseclient.domain.model.ClientProfile
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import com.example.medeaseclient.presentation.features.common.LoadingDialog
import com.example.medeaseclient.presentation.features.common.getSnackbarToastMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun HospitalProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    hospitalProfile: ClientProfile,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.profileState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.logoutFailure) {
        state.logoutFailure?.let {
            val errorMessage = getSnackbarToastMessage(state.logoutFailure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
        }
    }
    LaunchedEffect(key1 = state.authenticated) {
        if (!state.authenticated) {
            onLogoutClick.invoke()
        }
    }
    HospitalProfileScreenContent(
        hospitalProfile = hospitalProfile,
        state = state,
        snackbarHostState = snackbarHostState,
        onEditProfileClick = {},
        onLogoutClick = { viewModel.logout() },
        onBackClick = { onBackClick.invoke() }
    )
}

@Composable
fun HospitalProfileScreenContent(
    hospitalProfile: ClientProfile,
    state: ProfileStates,
    snackbarHostState: SnackbarHostState,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "Hospital Profile",
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingDialog(state.loggingOut)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = "Hospital Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = hospitalProfile.hospitalName ?: "Unknown",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = hospitalProfile.hospitalEmail ?: "Unknown",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                onClick = onEditProfileClick,
                shape = MaterialTheme.shapes.large,
                label = "Edit Profile",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(24.dp))
            ProfileSectionTitle(title = "More Details")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Column {
                    ProfileDetailRow("Hospital Id", hospitalProfile.hospitalId?.uppercase() ?: "Unknown")
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)
                    ProfileDetailRow("Phone", hospitalProfile.hospitalPhone ?: "Unknown")
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)
                    ProfileDetailRow("City", hospitalProfile.hospitalCity ?: "Unknown")
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)
                    ProfileDetailRow("Pincode", hospitalProfile.hospitalPinCode ?: "Unknown")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            SecondaryButton(
                onClick = onLogoutClick,
                shape = MaterialTheme.shapes.large,
                label = "Logout",
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
        )
    }
}

data class ProfileStates(
    val loggingOut: Boolean = false,
    val logoutFailure: LogoutFailure? = null,
    val authenticated: Boolean = true,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val clientHomeRepository: ClientHomeRepository,
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileStates())
    val profileState = _profileState.asStateFlow()

    fun logout() {
        _profileState.update { it.copy(loggingOut = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientHomeRepository.logout().onRight { isSuccess ->
                delay(1500)
                _profileState.update {
                    it.copy(
                        loggingOut = false,
                        authenticated = isSuccess.authenticated
                    )
                }
            }.onLeft { failure ->
                _profileState.update {
                    it.copy(
                        loggingOut = false,
                        logoutFailure = failure
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun HospitalProfileScreenPreview() {
    MedEaseTheme {
        HospitalProfileScreenContent(
            onEditProfileClick = {},
            onLogoutClick = {},
            hospitalProfile = ClientProfile(
                hospitalId = "12345jdsjff",
                hospitalName = "City Hospital",
                hospitalEmail = "cityhospital@example.com",
                hospitalPhone = "123-456-7890",
                hospitalCity = "New York",
                hospitalPinCode = "10001"
            ),
            onBackClick = {},
            snackbarHostState = remember { SnackbarHostState() },
            state = ProfileStates()
        )
    }

}