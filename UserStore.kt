package com.example.datastore.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userDataStore by preferencesDataStore(name = "user_data")

class UserDataStore(private val context: Context) {

    data class UserData(val username: String, val email: String, val id: String)

    suspend fun saveUserData(username: String, email: String, id: String) {
        context.userDataStore.edit { preferences ->
            preferences[Keys.USERNAME] = username
            preferences[Keys.EMAIL] = email
            preferences[Keys.ID] = id
        }
    }

    fun readUserData(): Flow<UserData> = context.userDataStore.data.map { preferences ->
        UserData(
            username = preferences[Keys.USERNAME] ?: "",
            email = preferences[Keys.EMAIL] ?: "",
            id = preferences[Keys.ID] ?: ""
        )
    }

    suspend fun clearUserData() {
        context.userDataStore.edit {
            it.clear()
        }
    }

    private object Keys {
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val ID = stringPreferencesKey("id")
    }
}
