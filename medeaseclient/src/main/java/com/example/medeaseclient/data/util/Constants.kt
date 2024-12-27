package com.example.medeaseclient.data.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


const val HOSPITALS_COLLECTION = "hospitals"
const val DOCTORS_COLLECTION = "doctors"


object PreferencesKeys {
    val CLIENT_REMEMBER_ME = booleanPreferencesKey("client_remember_me")
    val CLIENT_ID = stringPreferencesKey("client_id")
}