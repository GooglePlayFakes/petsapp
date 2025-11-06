package ru.topbun.pawmate.cache

import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

suspend fun Settings.getUserId(): Int? = this.data.map { it[AppSettings.KEY_USER_ID] }.firstOrNull()?.takeIf { it != -1 }
suspend fun Settings.saveUserId(id: Int?) = this.edit { it[AppSettings.KEY_USER_ID] = id ?: -1 }