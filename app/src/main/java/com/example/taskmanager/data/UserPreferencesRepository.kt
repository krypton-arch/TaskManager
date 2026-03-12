package com.example.taskmanager.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val AVATAR_COLOR = stringPreferencesKey("avatar_color")
        val THEME_PREFERENCE = stringPreferencesKey("theme_preference")
    }

    val userName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.USER_NAME] ?: "Daniel"
    }

    val avatarColor: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.AVATAR_COLOR] ?: "Purple"
    }

    val themePreference: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.THEME_PREFERENCE] ?: "System"
    }

    suspend fun updateUserName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.USER_NAME] = name
        }
    }

    suspend fun updateAvatarColor(color: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.AVATAR_COLOR] = color
        }
    }

    suspend fun updateThemePreference(theme: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.THEME_PREFERENCE] = theme
        }
    }
}
