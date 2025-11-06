package ru.topbun.pawmate.repository

import android.content.Context
import ru.topbun.pawmate.database.AppDatabase
import ru.topbun.pawmate.entity.Reminder
import ru.topbun.pawmate.workers.NotificationManager

class ReminderRepository(
    private val context: Context
) {

    private val dao = AppDatabase.getInstance(context).reminderDao()

    suspend fun getReminder(id: Int) = dao.getReminder(id)
    fun getReminderListFlow() = dao.getAllRemindersFlow()
    suspend fun getReminderList() = dao.getAllReminders()
    suspend fun addReminder(reminder: Reminder){
        dao.insertReminder(reminder)
        val notificationManager = NotificationManager()
        notificationManager.cancelReminder(context, reminder)
        notificationManager.scheduleReminder(context, reminder)
    }
    suspend fun deleteReminder(reminder: Reminder){
        dao.deleteReminder(reminder)
        val notificationManager = NotificationManager()
        notificationManager.cancelReminder(context, reminder)
    }
    suspend fun updateReminderState(id: Int, isActive: Boolean) = dao.updateReminderStatus(id, isActive)


}