package ru.topbun.pawmate.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

typealias Settings = DataStore<Preferences>

object AppSettings {

    private const val FILE_NAME = "settings"
    val Context.dataStore: Settings by preferencesDataStore(name = FILE_NAME)

    val KEY_USER_ID = intPreferencesKey(name = "user_id")

}