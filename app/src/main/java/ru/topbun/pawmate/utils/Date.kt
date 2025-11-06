package ru.topbun.pawmate.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun formatDate(timestamp: Long): String {
    val format = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault())
    val date = Date(timestamp)
    return format.format(date)
}