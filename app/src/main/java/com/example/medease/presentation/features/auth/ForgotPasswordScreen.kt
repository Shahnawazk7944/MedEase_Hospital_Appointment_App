package com.example.medease.presentation.features.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.components.OutlinedInputField
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medease.presentation.features.auth.viewmodels.AuthViewModel
import com.example.medease.presentation.features.auth.viewmodels.events.AuthEvent
import com.example.medease.presentation.features.auth.viewmodels.events.SignInEvent
import com.example.medease.presentation.features.auth.viewmodels.events.SignInStates
import com.example.medease.presentation.features.common.AuthHeadings
import com.example.medease.presentation.features.common.CustomTopBar
import com.example.medease.presentation.features.common.getSnackbarMessage

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.signInState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = state.isForgotPasswordLinkSent) {
        if (state.isForgotPasswordLinkSent) {
            viewModel.signInEvent(SignInEvent.ForgotPasswordEmailChanged(""))
            Toast.makeText(context, "Link sent to your email", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }

    LaunchedEffect(key1 = state.failure) {
        if (state.failure != null) {
            val errorMessage =
                getSnackbarMessage(state.failure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.signInEvent(SignInEvent.RemoveFailure(null))
        }
    }
    ForgotPasswordContent(
        state = state,
        snackbarHostState = snackbarHostState,
        signInEvent = viewModel::signInEvent,
        onBackClick = {
            navController.navigateUp()
        },
        onForgotPasswordClick = {
            viewModel.authEvent(AuthEvent.ForgotPasswordRequest(state.forgotPasswordEmail))
        }
    )
}

@Composable
fun ForgotPasswordContent(
    state: SignInStates,
    snackbarHostState: SnackbarHostState,
    signInEvent: (SignInEvent) -> Unit,
    onBackClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() }
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
                .padding(horizontal = MaterialTheme.spacing.mediumLarge)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
            AuthHeadings(
                heading = "Forgot Password!",
                subHeading = "We will send you a link to reset your password"
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            OutlinedInputField(
                value = state.forgotPasswordEmail,
                onChange = {
                    signInEvent(SignInEvent.ForgotPasswordEmailChanged(it))
                },
                label = "Email",
                placeholder = {
                    Text(
                        text = "AlexSmith@gmail.com",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                error = state.forgotPasswordEmailError,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PrimaryButton(
                label = "Send Forgot Password Link",
                onClick = {
                    onForgotPasswordClick.invoke()
                },
                enabled = state.forgotPasswordEmail.isNotBlank() && state.forgotPasswordEmailError == null && !state.loading,
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                isLoading = state.loading
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordContentPreview() {
    MedEaseTheme {
        ForgotPasswordContent(
            state = SignInStates(),
            snackbarHostState = remember { SnackbarHostState() },
            signInEvent = {},
            onBackClick = {},
            onForgotPasswordClick = {},
        )
    }
}