package ru.topbun.pawmate.presentation.screens.reminder

import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import ru.topbun.pawmate.entity.Pet
import ru.topbun.pawmate.entity.Reminder
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts
import ru.topbun.pawmate.presentation.theme.Typography
import ru.topbun.pawmate.presentation.theme.components.AppButton
import ru.topbun.pawmate.presentation.theme.components.AppTextField
import ru.topbun.pawmate.presentation.theme.components.DialogWrapper
import ru.topbun.pawmate.presentation.theme.components.noRippleClickable
import ru.topbun.pawmate.presentation.theme.components.rippleClickable
import ru.topbun.pawmate.utils.formatDate
import ru.topbun.pawmate.utils.pickImageLauncher
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.TemporalField
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun EditReminderDialog(reminder: Reminder, onSave: (Reminder) -> Unit, onDismiss: () -> Unit) {
    DialogWrapper(onDismissDialog = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var title by remember { mutableStateOf(reminder.title) }
            var descr by remember { mutableStateOf(reminder.description) }
            var frequency by remember { mutableStateOf(reminder.frequency.toString()) }
            var dateTime by remember { mutableStateOf(reminder.dateTime) }
            Title()
            Spacer(modifier = Modifier.height(16.dp))
            Field(
                title = title,
                onChangeTitle = {title = it},
                descr = descr,
                onChangeDescr = {descr = it},
                frequency = frequency,
                onChangeFrequency = {frequency = it},
                dateTime = formatDate(dateTime),
                onChangeDateTime = {dateTime = it},
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                text = "Добавить",
                enabled = true,
            ) {
                val newReminder = Reminder(
                    id = reminder.id,
                    title = title,
                    description = descr,
                    dateTime = dateTime,
                    frequency = frequency.toLongOrNull() ?: 0,
                    isActive = reminder.isActive
                )
                onSave(newReminder)
            }
        }
    }
}


@Composable
fun AddReminderDialog(onSave: (Reminder) -> Unit, onDismiss: () -> Unit) {
    DialogWrapper(onDismissDialog = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var title by remember { mutableStateOf("") }
            var descr by remember { mutableStateOf("") }
            var frequency by remember { mutableStateOf("") }
            var dateTime by remember { mutableStateOf(0L) }
            Title()
            Spacer(modifier = Modifier.height(16.dp))
            Field(
                title = title,
                onChangeTitle = {title = it},
                descr = descr,
                onChangeDescr = {descr = it},
                frequency = frequency,
                onChangeFrequency = {frequency = it},
                dateTime = formatDate(dateTime),
                onChangeDateTime = {dateTime = it},
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                text = "Добавить",
                enabled = true,
            ) {
                val reminder = Reminder(
                    title = title,
                    description = descr,
                    dateTime = dateTime,
                    frequency = frequency.toLongOrNull() ?: 0,
                )
                onSave(reminder)
            }
        }
    }
}

@Composable
private fun Field(
    title: String,
    onChangeTitle: (String) -> Unit,
    descr: String,
    onChangeDescr: (String) -> Unit,
    frequency: String,
    onChangeFrequency: (String) -> Unit,
    dateTime: String,
    onChangeDateTime: (Long) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppTextField(
            modifier = Modifier.height(48.dp),
            value = title,
            errorText = null,
            onValueChange = { onChangeTitle(it) },
            placeholder = "Заголовок",
        )
        AppTextField(
            modifier = Modifier.height(48.dp),
            value = descr,
            errorText = null,
            onValueChange = { onChangeDescr(it) },
            placeholder = "Описание",
        )
        AppTextField(
            modifier = Modifier.height(48.dp),
            value = frequency,
            errorText = null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { onChangeFrequency(it) },
            placeholder = "Периодичность в минутах",
        )
        AppTextField(
            modifier = Modifier
                .height(48.dp),
            value = dateTime,
            errorText = null,
            enabled = false,
            onValueChange = { },
            placeholder = "Напомнить в",
        )
        WheelDateTimePicker{
            val calendar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Calendar.getInstance(Locale.getDefault())
            } else {
                return@WheelDateTimePicker
            }
            calendar.set(it.year, it.monthValue - 1, it.dayOfMonth, it.hour, it.minute, it.second)
            onChangeDateTime(calendar.timeInMillis)
        }
    }
}

@Composable
private fun Title() {
    Text(
        text = "Добавить напоминание",
        style = Typography.APP_TEXT,
        color = Colors.BLACK,
        fontSize = 24.sp,
        fontFamily = Fonts.SF.SEMI_BOLD
    )
}
