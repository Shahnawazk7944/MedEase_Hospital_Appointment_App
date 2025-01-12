package com.example.designsystem.components

import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.designsystem.R
import com.example.designsystem.theme.spacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDate(
    onConfirmText: String = stringResource(R.string.select),
    onDismissText: String = stringResource(R.string.cancel),
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        //selectableDates = PastOrPresentSelectableDates
    )
    DatePickerDialog(
        shape = RoundedCornerShape(MaterialTheme.spacing.large),
        onDismissRequest = { onDismiss.invoke() },
        confirmButton = {
            PrimaryButton(
                label = onConfirmText,
                onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it.convertMillisToDate()) }
                    onDismiss()
                },
            )
        },
        dismissButton = {
            SecondaryButton(
                label = onDismissText,
                onClick = { onDismiss.invoke() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
            )
        },
        content = {
            DatePicker(
                state = datePickerState,
            )
        }
    )
}

fun Long.convertMillisToDate(): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}

@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"))
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfToday = calendar.timeInMillis
        return utcTimeMillis <= endOfToday
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTime(
    onConfirmText: String = stringResource(R.string.select),
    onDismissText: String = stringResource(R.string.cancel),
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )
    AlertDialog(
        shape = RoundedCornerShape(MaterialTheme.spacing.large),
        onDismissRequest = { onDismiss.invoke() },
        title = { Text("Select Time") },
        confirmButton = {
            PrimaryButton(
                label = onConfirmText,
                onClick = {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    calendar.set(Calendar.MINUTE, timePickerState.minute)

                    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val selectedTime = formatter.format(calendar.time)

                    onTimeSelected(
                        selectedTime
                    )
                    onDismiss()
                },
            )
        },
        dismissButton = {
            SecondaryButton(
                label = onDismissText,
                onClick = { onDismiss.invoke() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
            )
        },
        text = {
            Box(modifier = Modifier.padding(MaterialTheme.spacing.extraLarge)){ TimePicker(state = timePickerState) }
        }
    )
}