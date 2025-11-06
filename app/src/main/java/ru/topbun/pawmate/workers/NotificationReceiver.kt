package ru.topbun.pawmate.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.topbun.pawmate.entity.Reminder
import ru.topbun.pawmate.repository.ReminderRepository

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            val reminderId = intent.getIntExtra("reminder_id", 0)
            val reminder = getReminderFromDatabase(context, reminderId) ?: return@launch

            if (reminder.isActive) {
                val notificationManager = NotificationManager()
                notificationManager.showNotification(context, reminder)
            }
        }

    }

    private suspend fun getReminderFromDatabase(context: Context, reminderId: Int): Reminder? {
        val repository = ReminderRepository(context)
        return repository.getReminder(reminderId)
    }
}