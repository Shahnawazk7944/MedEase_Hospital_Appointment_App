package com.example.medeaseclient.data.repository.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import arrow.core.Either
import com.example.medeaseclient.data.util.PreferencesKeys
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ClientDataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ClientDataStoreRepository {

    override suspend fun getRememberMe(): Either<GetRememberMeAndIDPreferencesFailure, Boolean> {
        try {
            val preferences = dataStore.data.first()
            val rememberMe = preferences[PreferencesKeys.CLIENT_REMEMBER_ME] == true
            return if (rememberMe) Either.Right(true) else Either.Right(false)
        } catch (e: Exception) {
            return Either.Left(GetRememberMeAndIDPreferencesFailure.UnknownError)
        }
    }

    override suspend fun getClientId(): Either<GetRememberMeAndIDPreferencesFailure, String> {
        try {
            val preferences = dataStore.data.first()
            val id = preferences[PreferencesKeys.CLIENT_ID]
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