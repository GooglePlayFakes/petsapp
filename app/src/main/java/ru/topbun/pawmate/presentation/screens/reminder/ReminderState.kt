package ru.topbun.pawmate.presentation.screens.reminder

import ru.topbun.pawmate.entity.Reminder

data class ReminderState(
    val reminderList: List<Reminder> = emptyList()
)
