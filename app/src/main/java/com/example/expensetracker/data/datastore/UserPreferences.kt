package com.example.expensetracker.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val DATA = "User_Data"
        private val USERNAME = stringPreferencesKey("username")
        private val PROFILE = stringPreferencesKey("profileImage")
    }

    suspend fun savePreference(username: String, image: URI){
        dataStore.edit {  preferences ->
            preferences[PROFILE] = image.toString()
            preferences[USERNAME] = username
        }
    }
}