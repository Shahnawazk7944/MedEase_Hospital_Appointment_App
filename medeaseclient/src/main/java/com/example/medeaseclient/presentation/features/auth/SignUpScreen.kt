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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.medeaseclient.presentation.features.auth.components.AuthHeadings
import com.example.medeaseclient.presentation.features.auth.components.CustomTopBar
import com.example.medeaseclient.presentation.features.auth.components.SignUpTextFields
import com.example.medeaseclient.presentation.features.auth.utils.isSignUpFormValid
import com.example.medeaseclient.presentation.features.auth.viewmodels.AuthViewModel
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.AuthEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignUpEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignUpStates

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignInClick: () -> Unit,
    onSuccessFullSignUp: () -> Unit
) {
    val state by viewModel.signUpState.collectAsStateWithLifecycle()
    SignUpContent(
        state = state,
        authEvent = viewModel::authEvent,
        signUpEvent = viewModel::signUpEvent,
        onSignInClick = onSignInClick,
        onSuccessFullSignUp = onSuccessFullSignUp
    )
}

@Composable
fun SignUpContent(
    state: SignUpStates,
    authEvent: (AuthEvent) -> Unit,
    signUpEvent: (SignUpEvent) -> Unit,
    onSignInClick: () -> Unit,
    onSuccessFullSignUp: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = {}
            )
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
                        authEvent(
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
                        onSuccessFullSignUp.invoke()
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
            authEvent = {},
            signUpEvent = TODO(),
            onSignInClick = TODO(),
            onSuccessFullSignUp = TODO()
        )
    }
}