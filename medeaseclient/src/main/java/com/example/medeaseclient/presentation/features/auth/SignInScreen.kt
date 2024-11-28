package com.example.medeaseclient.presentation.features.auth

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.presentation.features.auth.components.AuthBottomActions
import com.example.medeaseclient.presentation.features.auth.components.AuthHeadings
import com.example.medeaseclient.presentation.features.auth.components.CustomTopBar
import com.example.medeaseclient.presentation.features.auth.components.SignInTextFields
import com.example.medeaseclient.presentation.features.auth.utils.getSnackbarMessage
import com.example.medeaseclient.presentation.features.auth.utils.isSignInFormValid
import com.example.medeaseclient.presentation.features.auth.utils.reset
import com.example.medeaseclient.presentation.features.auth.viewmodels.AuthViewModel
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.AuthEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignInEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignInStates
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignUpEvent
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignUpClick: () -> Unit,
    onSuccessFullLogin: () -> Unit
) {
    val state by viewModel.signInState.collectAsStateWithLifecycle()
    val activity = (LocalContext.current as? Activity)
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler {
        if (activity?.isTaskRoot == true) {
            activity.finishAndRemoveTask()
        }
    }
    LaunchedEffect(key1 = state.isSignInSuccess) {
        if (state.isSignInSuccess) {
            viewModel.signInEvent(SignInEvent.ClearAllFields(true))
            onSuccessFullLogin.invoke()
        }
    }
    LaunchedEffect(key1 = state.failure) {
        if (state.failure != null) {
            val errorMessage = getSnackbarMessage(state.failure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            delay(100)
            viewModel.signInEvent(SignInEvent.RemoveFailure(null))
        }
    }
    SignInContent(
        state = state,
        snackbarHostState = snackbarHostState,
        signInRequest = {
            viewModel.authEvent(
                AuthEvent.SignInRequest(
                    email = state.email,
                    password = state.password,
                    rememberMe = state.rememberMe
                )
            )
        },
        signInEvent = viewModel::signInEvent,
        onSignUpClick = onSignUpClick,
        onBackClick = {
            if (activity?.isTaskRoot == true) {
                activity.finishAndRemoveTask()
            }
        }
    )
}

@Composable
fun SignInContent(
    state: SignInStates,
    snackbarHostState: SnackbarHostState,
    signInRequest: () -> Unit,
    signInEvent: (SignInEvent) -> Unit,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit
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
            ){
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    snackbarData = it,
                    actionColor = MaterialTheme.colorScheme.secondary,
                    dismissActionContentColor = MaterialTheme.colorScheme.onSecondary
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
                heading = "Welcome Back!",
                subHeading = "Login to hospital account"
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
            SignInTextFields(
                state = state,
                event = signInEvent
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PrimaryButton(
                label = "Sign In",
                onClick = {
                    signInRequest.invoke()
                },
                enabled = state.isSignInFormValid() && !state.loading,
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                isLoading = state.loading
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            AuthBottomActions(
                actionText = "Sign Up",
                actionHeading = "Don't have an account?",
                onChangeAction = { onSignUpClick.invoke() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInContentPreview() {
    MedEaseTheme {
        SignInContent(
            state = SignInStates(),
            signInRequest = {},
            signInEvent = {},
            onSignUpClick = {},
            snackbarHostState = TODO(),
            onBackClick = TODO(),
        )
    
    }
}