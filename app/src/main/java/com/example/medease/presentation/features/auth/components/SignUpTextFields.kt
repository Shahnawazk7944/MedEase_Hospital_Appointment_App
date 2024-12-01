package com.example.medease.presentation.features.auth.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import com.example.medease.presentation.features.auth.viewmodels.events.SignUpEvent
import com.example.medease.presentation.features.auth.viewmodels.events.SignUpStates

@Composable
fun SignUpTextFields(
    state: SignUpStates,
    event: (SignUpEvent) -> Unit
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }
    val passwordTrailingIcon =
        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
    val confirmPasswordTrailingIcon =
        if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    OutlinedInputField(
        value = state.name,
        onChange = {
            event(SignUpEvent.NameChanged(it))
        },
        label = "Name",
        placeholder = {
            Text(
                text = "Alex Smith",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "person icon",
                modifier = Modifier.size(20.dp)
            )
        },
        error = state.nameError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    OutlinedInputField(
        value = state.email,
        onChange = {
            event(SignUpEvent.EmailChanged(it))
        },
        label = "Email",
        placeholder = {
            Text(
                text = "alex@gmail.com",
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
        value = state.phone,
        onChange = {
            event(SignUpEvent.PhoneChanged(it))
        },
        label = "Phone",
        placeholder = {
            Text(
                text = "+913001234567",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Phone icon",
                modifier = Modifier.size(20.dp)
            )
        },
        error = state.phoneError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    OutlinedInputField(
        value = state.password,
        onChange = {
            event(SignUpEvent.PasswordChanged(it))
        },
        label = "Password",
        placeholder = {
            Text(
                text = "Alex@123",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Password,
                contentDescription = "password icon",
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
    OutlinedInputField(
        value = state.confirmPassword,
        onChange = {
            event(SignUpEvent.ConfirmPasswordChanged(it))
        },
        label = "Confirm Password",
        placeholder = {
            Text(
                text = "Alex@123",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.ConfirmationNumber,
                contentDescription = "Password icon",
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                Icon(
                    imageVector = confirmPasswordTrailingIcon,
                    contentDescription = "eye icon",
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        error = state.confirmPasswordError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    RememberMeStatus(
        title = "Remember Me",
        currentStatus = state.rememberMe,
        onStatusChanged = {
            event(SignUpEvent.RememberMeChanged(it))
        }
    )
}