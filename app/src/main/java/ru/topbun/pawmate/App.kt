package ru.topbun.pawmate

import android.app.Application
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.topbun.pawmate.entity.Reminder
import ru.topbun.pawmate.workers.NotificationManager


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        val notificationManager = NotificationManager()
        notificationManager.startScheduleReminders(this)
    }
}