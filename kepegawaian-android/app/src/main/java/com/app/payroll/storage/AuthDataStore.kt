package com.app.payroll.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthDataStore(private val context: Context) {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val ROLE_KEY = stringPreferencesKey("user_role")

    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    val role: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ROLE_KEY]
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[ROLE_KEY] = role
        }
    }

    suspend fun clearAuth() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(ROLE_KEY)
        }
    }
}
