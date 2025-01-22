package com.example.medeaseclient.presentation.features.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    event: (SignInEvent) -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    val passwordTrailingIcon =
        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

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
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password icon",
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = passwordTrailingIcon,
                    contentDescription = "eye icon",
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        error = state.passwordError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RememberMeStatus(
            title = "Remember Me",
            currentStatus = state.rememberMe,
            onStatusChanged = {
                event(SignInEvent.RememberMeChanged(it))
            }
        )
        Text(
            text = "Forgot Password?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { onForgotPasswordClick.invoke() }
                .padding(horizontal = MaterialTheme.spacing.small)
        )
    }

}