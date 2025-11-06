package ru.topbun.pawmate.presentation.screens.reminder

import android.content.Context
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import ru.topbun.pawmate.entity.Reminder
import ru.topbun.pawmate.presentation.theme.components.ScreenModelState
import ru.topbun.pawmate.repository.ReminderRepository

class ReminderViewModel(
    context: Context
): ScreenModelState<ReminderState>(ReminderState()) {

    private val repository = ReminderRepository(context)

    private fun loadReminders() = screenModelScope.launch {
        repository.getReminderListFlow().collect{
            updateState { copy(it) }
        }
    }

    fun addReminder(reminder: Reminder) {
        screenModelScope.launch { repository.addReminder(reminder) }
    }

    fun deleteReminder(reminder: Reminder) {
        screenModelScope.launch { repository.deleteReminder(reminder) }
    }

    fun updateReminderStatus(id: Int, isActive: Boolean) {
        screenModelScope.launch { repository.updateReminderState(id, isActive) }
    }

    init {
        loadReminders()
    }

}