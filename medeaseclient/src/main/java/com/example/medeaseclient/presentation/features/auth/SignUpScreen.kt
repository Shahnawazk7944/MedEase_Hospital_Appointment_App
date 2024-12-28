package com.example.medeaseclient.presentation.features.auth

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.components.PrimaryButton
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.presentation.features.auth.components.AuthBottomActions
import com.example.medeaseclient.presentation.features.common.AuthHeadings
import com.example.medeaseclient.presentation.features.common.CustomTopBar
import com.example.medeaseclient.presentation.features.auth.components.SignUpTextFields
import com.example.medeaseclient.presentation.features.common.getSnackbarToastMessage
import com.example.medeaseclient.presentation.features.auth.utils.isSignUpFormValid
import com.example.medeaseclient.presentation.features.auth.viewmodels.AuthViewModel
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.AuthEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignUpEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignUpStates

/**
 * HospitalAdmin1@city.com - Admin1@123
 * HospitalAdmin2@city.com - Admin2@123
 * HospitalAdmin3@city.com - Admin3@123
 * HospitalAdmin4@city.com - Admin4@123
 */
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignInClick: () -> Unit,
    onSuccessFullSignUp: () -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.signUpState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.isSignUpSuccess) {
        if (state.isSignUpSuccess) {
            viewModel.signUpEvent(SignUpEvent.ClearAllFields(true))
            onSuccessFullSignUp.invoke()
        }
    }
    LaunchedEffect(key1 = state.failure) {
        if (state.failure != null) {
            val errorMessage = getSnackbarToastMessage(state.failure)
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.signUpEvent(SignUpEvent.RemoveFailure(null))
        }
    }

    SignUpContent(
        state = state,
        signUpRequest = {
            viewModel.authEvent(
                AuthEvent.SignUpRequest(
                    hospitalName = state.hospitalName,
                    hospitalEmail = state.hospitalEmail,
                    hospitalPhone = state.hospitalPhone,
                    hospitalCity = state.hospitalCity,
                    hospitalPinCode = state.hospitalPinCode,
                    password = state.hospitalPassword,
                    rememberMe = state.rememberMe
                )
            )
        },
        signUpEvent = viewModel::signUpEvent,
        onSignInClick = onSignInClick,
        onBackClick = onBackClick,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun SignUpContent(
    state: SignUpStates,
    snackbarHostState: SnackbarHostState,
    signUpRequest: () -> Unit,
    signUpEvent: (SignUpEvent) -> Unit,
    onSignInClick: () -> Unit,
    onBackClick: () -> Unit,
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
                    dismissActionContentColor = MaterialTheme.colorScheme.secondary
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.Transparent)
                    .padding(horizontal = MaterialTheme.spacing.mediumLarge)
            ) {
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
                subHeading = "Let's create hospital account together"
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
            SignUpTextFields(
                state = state,
                event = signUpEvent
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
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
            onBackClick = TODO(),
            snackbarHostState = TODO()
        )
    }
}