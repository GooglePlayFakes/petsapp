package ru.topbun.pawmate.presentation.screens.reminder

import android.widget.Space
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.topbun.pawmate.R
import ru.topbun.pawmate.entity.Reminder
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.Fonts
import ru.topbun.pawmate.presentation.theme.Typography
import ru.topbun.pawmate.presentation.theme.components.AppButton
import ru.topbun.pawmate.presentation.theme.components.AppIcon
import ru.topbun.pawmate.presentation.theme.components.noRippleClickable
import ru.topbun.pawmate.utils.formatDate

object ReminderScreen : Screen {

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var openAddReminderModal by remember { mutableStateOf(false) }
            val context = LocalContext.current
            val viewModel = rememberScreenModel { ReminderViewModel(context) }
            val state by viewModel.state.collectAsState()
            Header()
            Spacer(modifier = Modifier.height(4.dp))
            ReminderList(viewModel)
            AppButton(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                text = "Добавить напоминание",
                enabled = true,
            ) {
                openAddReminderModal = true
            }
            if (openAddReminderModal) {
                AddReminderDialog(
                    onDismiss = { openAddReminderModal = false },
                    onSave = { openAddReminderModal = false; viewModel.addReminder(it) }
                )
            }
        }
    }

}

@Composable
private fun ColumnScope.ReminderList(viewModel: ReminderViewModel) {
    val state by viewModel.state.collectAsState()
    var indexOpenDetailReminder by rememberSaveable { mutableStateOf<Int?>(null) }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(state.reminderList) { index, item ->
            ReminderItem(
                viewModel = viewModel,
                reminder = item,
                isOpen = indexOpenDetailReminder == index,
                onClick = {
                    indexOpenDetailReminder = if (indexOpenDetailReminder == index) null else index
                }
            )
        }
    }
}

@Composable
private fun ReminderItem(
    viewModel: ReminderViewModel,
    reminder: Reminder,
    isOpen: Boolean,
    onClick: () -> Unit,
) {
    var isOpenEditReminder by rememberSaveable { mutableStateOf(false) }
    if (isOpenEditReminder){
        EditReminderDialog(
            reminder = reminder,
            onSave = {
                isOpenEditReminder = false
                viewModel.addReminder(it)
            }
        ) {
            isOpenEditReminder = false
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.WHITE)
            .border(1.5.dp, Colors.BROWN, RoundedCornerShape(12.dp))
            .noRippleClickable {
                onClick()
            }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = reminder.title,
                style = Typography.APP_TEXT,
                color = Colors.BLACK,
                fontSize = 20.sp,
                fontFamily = Fonts.SF.SEMI_BOLD
            )
            Text(
                text = reminder.description,
                style = Typography.APP_TEXT,
                color = Colors.BLACK,
                fontSize = 16.sp,
                fontFamily = Fonts.SF.MEDIUM,
                maxLines = if (isOpen) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )
            if (isOpen) {
                Text(
                    text = buildAnnotatedString {
                        append("Напоминание в: ")
                        withStyle(
                            SpanStyle(
                                color = Colors.BROWN,
                            )
                        ) {
                            append(formatDate(reminder.dateTime))
                        }
                    },
                    style = Typography.APP_TEXT,
                    color = Colors.BLACK,
                    fontSize = 16.sp,
                    fontFamily = Fonts.SF.SEMI_BOLD
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Switch(
                checked = reminder.isActive,
                onCheckedChange = { viewModel.updateReminderStatus(reminder.id, !reminder.isActive) },
                modifier = Modifier.padding(10.dp),
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Colors.BROWN,
                    uncheckedThumbColor = Colors.BROWN,
                    uncheckedTrackColor = Colors.WHITE,
                    uncheckedBorderColor = Colors.BROWN
                )
            )
            if (isOpen) {
                Row{
                    AppIcon(R.drawable.ic_edit) {
                        isOpenEditReminder = true
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    AppIcon(R.drawable.ic_delete) {
                        viewModel.deleteReminder(reminder)
                    }
                }
            }
        }
    }
}


    @Composable
    private fun Header() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val navigator = LocalNavigator.currentOrThrow
            AppIcon(R.drawable.ic_back) {
                navigator.pop()
            }
            Text(
                text = "Напоминание",
                style = Typography.APP_TEXT,
                color = Colors.BLACK,
                fontSize = 24.sp,
                fontFamily = Fonts.SF.BOLD
            )
        }
    }