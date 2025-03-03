package com.app.quauhtlemallan.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USERNAME_KEY = stringPreferencesKey("username")
        val LOGIN_PROVIDER = stringPreferencesKey("login_provider") // "google", "facebook" o "email"
    }

    suspend fun saveSession(isLoggedIn: Boolean, username: String, provider: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
            preferences[USERNAME_KEY] = username
            preferences[LOGIN_PROVIDER] = provider
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences.remove(USERNAME_KEY)
            preferences.remove(LOGIN_PROVIDER)
        }
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    val username: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USERNAME_KEY]
        }

    val loginProvider: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LOGIN_PROVIDER]
        }
}
