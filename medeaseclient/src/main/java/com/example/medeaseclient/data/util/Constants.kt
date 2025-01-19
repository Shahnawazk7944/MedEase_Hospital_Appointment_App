package com.example.medeaseclient.data.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


const val HOSPITALS_COLLECTION = "hospitals"
const val DOCTORS_COLLECTION = "doctors"
const val BEDS_COLLECTION = "beds"
const val APPOINTMENTS_COLLECTION = "appointments"
const val USERS_COLLECTION = "users"
const val TRANSACTIONS_COLLECTION = "transactions"
const val USER_HEALTH_RECORDS_COLLECTION = "healthRecords"

object PreferencesKeys {
    val CLIENT_REMEMBER_ME = booleanPreferencesKey("client_remember_me")
    val CLIENT_ID = stringPreferencesKey("client_id")
}