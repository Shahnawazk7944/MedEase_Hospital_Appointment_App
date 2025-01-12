package com.example.medease.data.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val USERS_COLLECTION = "users"
const val HOSPITALS_COLLECTION = "hospitals"
const val DOCTORS_COLLECTION = "doctors"
const val BEDS_COLLECTION = "beds"
const val APPOINTMENTS_COLLECTION = "appointments"
const val TRANSACTIONS_COLLECTION = "transactions"


object PreferencesKeys {
    val USER_REMEMBER_ME = booleanPreferencesKey("user_remember_me")
    val USER_ID = stringPreferencesKey("user_id")
}