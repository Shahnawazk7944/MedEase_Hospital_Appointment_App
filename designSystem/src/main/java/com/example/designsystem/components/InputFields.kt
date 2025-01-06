package com.example.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.MedEaseTheme
import com.example.designsystem.theme.spacing


@Composable
fun OutlinedInputField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    error: String? = null,
    shape: Shape = MaterialTheme.shapes.small,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = error != null,
            shape = shape,
            enabled = enabled,
            readOnly = readOnly,
            visualTransformation = visualTransformation,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
                focusedLeadingIconColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.outline,
                errorLeadingIconColor = MaterialTheme.colorScheme.outline,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                focusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                disabledTrailingIconColor = MaterialTheme.colorScheme.outline,
                errorTrailingIconColor = MaterialTheme.colorScheme.outline,
                focusedPlaceholderColor =MaterialTheme.colorScheme.outline,
                unfocusedPlaceholderColor =MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor =MaterialTheme.colorScheme.outline,
                errorPlaceholderColor =MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.errorContainer,
            )
        )

        InputFieldError(error)
    }
}

@Composable
fun OutlinedDateInputField(
    label: String,
    date: String,
    onDateChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    error: String? = null,
    shape: Shape = MaterialTheme.shapes.small,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var openDatePicker by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        OutlinedTextField(
            value = date,
            onValueChange = {},
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = error != null,
            shape = shape,
            enabled = enabled,
            readOnly = true,
            visualTransformation = VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
                focusedLeadingIconColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.outline,
                errorLeadingIconColor = MaterialTheme.colorScheme.outline,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                focusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                disabledTrailingIconColor = MaterialTheme.colorScheme.outline,
                errorTrailingIconColor = MaterialTheme.colorScheme.outline,
                focusedPlaceholderColor =MaterialTheme.colorScheme.outline,
                unfocusedPlaceholderColor =MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor =MaterialTheme.colorScheme.outline,
                errorPlaceholderColor =MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.errorContainer,
            )
        )

        InputFieldError(error)
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Press) {
                openDatePicker = true
            }
        }
    }

    if (openDatePicker) {
        SelectDate(
            onDismiss = {
                openDatePicker = false
            },
            onDateSelected = { selectedDate ->
                onDateChange(selectedDate)
                openDatePicker = false
            },
        )
    }
}

@Composable
fun OutlinedTimeInputField(
    label: String,
    time: String,
    onTimeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    error: String? = null,
    shape: Shape = MaterialTheme.shapes.small,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var openTimePicker by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        OutlinedTextField(
            value = time,
            onValueChange = {},
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = error != null,
            shape = shape,
            enabled = enabled,
            readOnly = true,
            visualTransformation = VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
                focusedLeadingIconColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.outline,
                errorLeadingIconColor = MaterialTheme.colorScheme.outline,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                focusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                disabledTrailingIconColor = MaterialTheme.colorScheme.outline,
                errorTrailingIconColor = MaterialTheme.colorScheme.outline,
                focusedPlaceholderColor =MaterialTheme.colorScheme.outline,
                unfocusedPlaceholderColor =MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor =MaterialTheme.colorScheme.outline,
                errorPlaceholderColor =MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.errorContainer,
            )
        )

        InputFieldError(error)
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Press) {
                openTimePicker = true
            }
        }
    }

    if (openTimePicker) {
        SelectTime(
            onDismiss = {
                openTimePicker = false
            },
            onTimeSelected = { selectedTime ->
                onTimeChange(selectedTime)
                openTimePicker = false
            },
        )
    }
}

@Composable
private fun InputFieldError(
    error: String?,
) {
    AnimatedVisibility(
        visible = error != null,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.small),
            )
        }
    }
}

@Preview
@Composable
private fun InputFieldsPreview() {
    MedEaseTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedInputField(
                label = "Email",
                value = "",
                onChange = {},
                placeholder = {
                    Text("placeholder@mail.com")
                }
            )

            OutlinedInputField(
                label = "Email",
                value = "actual@mail.com",
                onChange = {}
            )

            OutlinedInputField(
                label = "Email",
                value = "",
                onChange = {},
                placeholder = {
                    Text("placeholder@mail.com")
                },
                error = "Email is required"
            )

            OutlinedInputField(
                label = "Password",
                value = "",
                onChange = {},
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null
                    )
                }
            )

            OutlinedInputField(
                label = "Password",
                value = "",
                onChange = {},
                error = "Password must be at least 8 characters long.",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null
                    )
                }
            )
        }
    }
}
