package ru.topbun.pawmate.workers

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.topbun.pawmate.R
import ru.topbun.pawmate.entity.Reminder
import ru.topbun.pawmate.repository.ReminderRepository

class NotificationManager {

    fun startScheduleReminders(context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            val repository = ReminderRepository(context)
            repository.getReminderList().forEach {
                scheduleReminder(context, it)
            }
        }
    }

    fun scheduleReminder(context: Context, reminder: Reminder) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("reminder_id", reminder.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (reminder.frequency == 0L) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    reminder.dateTime,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    reminder.dateTime,
                    pendingIntent
                )
            }
        } else {
            val startTime = calculateNextTriggerTime(reminder.dateTime, reminder.frequency)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                startTime,
                reminder.frequency,
                pendingIntent
            )
        }
    }

    private fun calculateNextTriggerTime(startTime: Long, frequency: Long): Long {
        val currentTime = System.currentTimeMillis()
        var nextTime = startTime
        while (nextTime < currentTime) {
            nextTime += frequency
        }
        return nextTime
    }

    fun cancelReminder(context: Context, reminder: Reminder) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun showNotification(context: Context, reminder: Reminder) {
        CoroutineScope(Dispatchers.IO).launch {
            val notificationManager = getSystemService(context, NotificationManager::class.java) as NotificationManager
            createNotificationChannel(notificationManager)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(reminder.title)
                .setContentText(reminder.description)
                .setSmallIcon(R.drawable.ic_notification)
                .build()
            notificationManager.notify(reminder.id, notification)
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "reminder_water_id"
        private const val CHANNEL_NAME = "reminder_water"
    }
}
