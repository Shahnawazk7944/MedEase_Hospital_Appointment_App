package com.example.medease.presentation.features.auth

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medease.presentation.features.auth.components.AuthBottomActions
import com.example.medease.presentation.features.auth.components.AuthHeadings
import com.example.medease.presentation.features.auth.components.CustomTopBar
import com.example.medease.presentation.features.auth.components.SignUpTextFields
import com.example.medease.presentation.features.auth.utils.getSnackbarMessage
import com.example.medease.presentation.features.auth.utils.isSignUpFormValid
import com.example.medease.presentation.features.auth.utils.reset
import com.example.medease.presentation.features.auth.viewmodels.AuthViewModel
import com.example.medease.presentation.features.auth.viewmodels.events.AuthEvent
import com.example.medease.presentation.features.auth.viewmodels.events.SignUpEvent
import com.example.medease.presentation.features.auth.viewmodels.events.SignUpStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * User1@city.com - Admin1@123
 * User2@city.com - Admin2@123
 * User3@city.com - Admin3@123
 * User4@city.com - Admin4@123
 */
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignInClick: () -> Unit,
    onSuccessFullSignUp: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.signUpState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = state.isSignUpSuccess) {
        if (state.isSignUpSuccess) {
            viewModel.signUpEvent(SignUpEvent.ClearAllFields(true))
            onSuccessFullSignUp.invoke()
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
            viewModel.signUpEvent(SignUpEvent.RemoveFailure(null))
        }
    }
    SignUpContent(
        state = state,
        snackbarHostState = snackbarHostState,
        signUpRequest = {
            viewModel.viewModelScope.launch {
                viewModel.authEvent(
                    AuthEvent.SignUpRequest(
                        name = state.name,
                        email = state.email,
                        phone = state.phone,
                        password = state.password,
                        rememberMe = state.rememberMe
                    )
                )
            }
        },
        signUpEvent = viewModel::signUpEvent,
        onSignInClick = onSignInClick,
        onBackClick = onBackClick
    )
}

@Composable
fun SignUpContent(
    state: SignUpStates,
    snackbarHostState: SnackbarHostState,
    signUpRequest: () -> Unit,
    signUpEvent: (SignUpEvent) -> Unit,
    onSignInClick: () -> Unit,
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
                heading = "Create Account",
                subHeading = "Let's create your account together"
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
            SignUpTextFields(
                state = state,
                event = signUpEvent
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PrimaryButton(
                label = "Sign Up",
                onClick = {
                    signUpRequest.invoke()
                },
                enabled = state.isSignUpFormValid() && !state.loading,
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                isLoading = state.loading
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            AuthBottomActions(
                actionText = "Sign In",
                actionHeading = "Already have an account?",
                onChangeAction = { onSignInClick.invoke() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpContentPreview() {
    MedEaseTheme {
        SignUpContent(
            state = SignUpStates(),
            signUpRequest = {},
            signUpEvent = TODO(),
            onSignInClick = TODO(),
            snackbarHostState = TODO(),
            onBackClick = TODO(),
        )
    }
}