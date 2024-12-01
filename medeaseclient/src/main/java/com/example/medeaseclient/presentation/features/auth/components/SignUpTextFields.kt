package com.example.medeaseclient.presentation.features.auth.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Pin
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
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignUpEvent
import com.example.medeaseclient.presentation.features.auth.viewmodels.events.SignUpStates

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
        value = state.hospitalName,
        onChange = {
            event(SignUpEvent.HospitalNameChanged(it))
        },
        label = "Hospital Name",
        placeholder = {
            Text(
                text = "City Care",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = "Hospital icon",
                modifier = Modifier.size(20.dp)
            )
        },
        error = state.hospitalNameError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    OutlinedInputField(
        value = state.hospitalEmail,
        onChange = {
            event(SignUpEvent.HospitalEmailChanged(it))
        },
        label = "Hospital Email",
        placeholder = {
            Text(
                text = "city@hospital.com",
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
        error = state.hospitalEmailError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    OutlinedInputField(
        value = state.hospitalPhone,
        onChange = {
            event(SignUpEvent.HospitalPhoneChanged(it))
        },
        label = "Hospital Phone",
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
        error = state.hospitalPhoneError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    OutlinedInputField(
        value = state.hospitalCity,
        onChange = {
            event(SignUpEvent.HospitalCityChanged(it))
        },
        label = "Hospital City",
        placeholder = {
            Text(
                text = "Mumbai",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AddLocation,
                contentDescription = "Location icon",
                modifier = Modifier.size(20.dp)
            )
        },
        error = state.hospitalCityError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    OutlinedInputField(
        value = state.hospitalPinCode,
        onChange = {
            event(SignUpEvent.HospitalPinCodeChanged(it))
        },
        label = "Hospital Pin Code",
        placeholder = {
            Text(
                text = "400001",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Pin,
                contentDescription = "PinCode icon",
                modifier = Modifier.size(20.dp)
            )
        },
        error = state.hospitalPinCodeError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    OutlinedInputField(
        value = state.hospitalPassword,
        onChange = {
            event(SignUpEvent.HospitalPasswordChanged(it))
        },
        label = "Password",
        placeholder = {
            Text(
                text = "City@123",
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
        error = state.hospitalPasswordError,
        maxLines = 1
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    OutlinedInputField(
        value = state.hospitalConfirmPassword,
        onChange = {
            event(SignUpEvent.HospitalConfirmPasswordChanged(it))
        },
        label = "Confirm Password",
        placeholder = {
            Text(
                text = "City@123",
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
        error = state.hospitalConfirmPasswordError,
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