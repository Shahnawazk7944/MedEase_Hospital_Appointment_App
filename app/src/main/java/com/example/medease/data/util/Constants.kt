package com.example.medease.data.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val USERS_COLLECTION = "users"

object PreferencesKeys {
    val USER_REMEMBER_ME = booleanPreferencesKey("user_remember_me")
    val USER_ID = stringPreferencesKey("user_id")
}