package com.example.medeaseclient.presentation.features.auth.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.OutlinedInputField
import com.example.designsystem.theme.spacing
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignInEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignInStates

@Composable
fun SignInTextFields(
    state: SignInStates,
    event: (SignInEvent) -> Unit
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    val passwordTrailingIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    OutlinedInputField(
        value = state.email,
        onChange = {
            event(SignInEvent.EmailChanged(it))
        },
        label = "Email",
        placeholder = {
            Text(
                text = "AlexSmith@gmail.com",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email icon",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        },
        error = state.emailError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    OutlinedInputField(
        value = state.password,
        onChange = {
            event(SignInEvent.PasswordChanged(it))
        },
        label = "Password",
        placeholder = {
            Text(
                text = "Alex123@",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password icon",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = passwordTrailingIcon,
                    contentDescription = "eye icon",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        error = state.passwordError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    RememberMeStatus(
        title = "Remember Me",
        currentStatus = state.rememberMe,
        onStatusChanged = {
            event(SignInEvent.RememberMeChanged(it))
        }
    )

}