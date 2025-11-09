package ru.topbun.pawmate.presentation.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import ru.topbun.pawmate.entity.Note
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts
import ru.topbun.pawmate.presentation.theme.Typography
import ru.topbun.pawmate.presentation.theme.components.AppButton
import ru.topbun.pawmate.presentation.theme.components.DialogWrapper

@Composable
fun ConfirmDeleteNoteModal(
    note: Note,
    onDialogDismiss: () -> Unit,
    onConfirm: (Note) -> Unit
) {
    DialogWrapper(onDismissDialog = onDialogDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Title()
            Spacer(Modifier.height(16.dp))
            Text(
                text = note.title,
                style = Typography.APP_TEXT,
                color = Colors.BROWN,
                fontSize = 20.sp,
                fontFamily = Fonts.SF.SEMI_BOLD
            )
            Spacer(Modifier.height(16.dp))
            Row {
                AppButton(
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    text = "Отмена",
                    enabled = true,
                    containerColor = Colors.WHITE,
                    contentColor = Colors.BLACK
                ) {
                    onDialogDismiss()
                }
                Spacer(Modifier.width(16.dp))
                AppButton(
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    text = "Удалить",
                    enabled = true,
                    containerColor = Colors.WHITE,
                    contentColor = Colors.RED
                ) {
                    onConfirm(note)
                }
            }
        }
    }
}

@Composable
private fun Title() {
    Text(
        text = "Удалить заметку?",
        style = Typography.APP_TEXT,
        color = Colors.BLACK,
        fontSize = 24.sp,
        fontFamily = Fonts.SF.SEMI_BOLD
    )
}