package ru.topbun.pawmate.presentation.screens.detail

import android.R.attr.name
import android.icu.util.Calendar
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import ru.topbun.pawmate.entity.Note
import ru.topbun.pawmate.entity.Reminder
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts
import ru.topbun.pawmate.presentation.theme.Typography
import ru.topbun.pawmate.presentation.theme.components.AppButton
import ru.topbun.pawmate.presentation.theme.components.AppTextField
import ru.topbun.pawmate.presentation.theme.components.DialogWrapper
import java.util.Collections.frequency
import java.util.Locale

@Composable
fun AddNoteModal(
    petId: Int,
    onDialogDismiss: () -> Unit,
    onAddNote: (Note) -> Unit
) {
    DialogWrapper(onDismissDialog = onDialogDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var title by remember { mutableStateOf("") }
            var descr by remember { mutableStateOf("") }
            var dateTime by remember { mutableStateOf(0L) }

            Title()
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                AppTextField(
                    modifier = Modifier.height(48.dp),
                    value = title,
                    errorText = null,
                    onValueChange = { title = it },
                    placeholder = "Заголовок",
                )
                AppTextField(
                    modifier = Modifier.height(160.dp),
                    value = descr,
                    errorText = null,
                    singleLine = false,
                    onValueChange = { descr = it },
                    placeholder = "Описание",
                )
                WheelDateTimePicker{
                    val calendar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Calendar.getInstance(Locale.getDefault())
                    } else {
                        return@WheelDateTimePicker
                    }
                    calendar.set(it.year, it.monthValue - 1, it.dayOfMonth, it.hour, it.minute, it.second)
                    dateTime = calendar.timeInMillis
                }
            }
            Spacer(Modifier.height(16.dp))
            AppButton(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                text = "Добавить",
                enabled = true,
            ) {
                val note = Note(
                    title = title,
                    petId = petId,
                    descr = descr,
                    date = dateTime,
                )
                onAddNote(note)
            }
        }
    }
}

@Composable
private fun Title() {
    Text(
        text = "Добавить Заметку",
        style = Typography.APP_TEXT,
        color = Colors.BLACK,
        fontSize = 24.sp,
        fontFamily = Fonts.SF.SEMI_BOLD
    )
}