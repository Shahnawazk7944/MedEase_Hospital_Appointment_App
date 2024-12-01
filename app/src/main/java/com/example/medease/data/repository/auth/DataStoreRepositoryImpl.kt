package com.example.medease.data.repository.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import arrow.core.Either
import com.example.medease.data.util.PreferencesKeys
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserDataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserDataStoreRepository {

    override suspend fun getRememberMe(): Either<GetRememberMeAndIDPreferencesFailure, Boolean> {
        try {
            val preferences = dataStore.data.first()
            val rememberMe = preferences[PreferencesKeys.USER_REMEMBER_ME] == true
            return if (rememberMe) Either.Right(true) else Either.Right(false)
        } catch (e: Exception) {
            return Either.Left(GetRememberMeAndIDPreferencesFailure.UnknownError)
        }
    }

    override suspend fun getUserId(): Either<GetRememberMeAndIDPreferencesFailure, String> {
        try {
            val preferences = dataStore.data.first()
            val id = preferences[PreferencesKeys.USER_ID]
            return if (id != null && id.isNotBlank()) {
                Either.Right(id)
            } else {
                Either.Left(GetRememberMeAndIDPreferencesFailure.UnknownError)
            }
        } catch (e: Exception) {
            return Either.Left(GetRememberMeAndIDPreferencesFailure.UnknownError)
        }
    }
}