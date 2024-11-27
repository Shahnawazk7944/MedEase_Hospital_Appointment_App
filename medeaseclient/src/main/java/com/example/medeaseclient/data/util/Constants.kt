package com.example.medeaseclient.data.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

//const val DATABASE_NAME = "prod"
//const val PROFILE_DATABASE_NAME = "userProfile"
//const val PROFILE_COLLECTION = "profileList"

const val HOSPITALS_COLLECTION = "hospitals"


object PreferencesKeys {
    val CLIENT_REMEMBER_ME = booleanPreferencesKey("client_remember_me")
    val CLIENT_ID = stringPreferencesKey("client_id")
}