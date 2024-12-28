package com.example.medeaseclient.data.repository.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import arrow.core.Either
import com.example.medeaseclient.data.firebase.FirebaseWrapper
import com.example.medeaseclient.data.repository.auth.AuthSuccess
import com.example.medeaseclient.data.util.HOSPITALS_COLLECTION
import com.example.medeaseclient.data.util.PreferencesKeys
import com.example.medeaseclient.domain.model.ClientProfile
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientHomeRepositoryImpl @Inject constructor(
    private val firebaseWrapper: FirebaseWrapper,
    private val dataStore: DataStore<Preferences>
) : ClientHomeRepository {
    private val auth = firebaseWrapper.authUser
    private val firestore = firebaseWrapper.firestore

    override suspend fun getClientProfile(clientId: String): Either<ClientProfileFailure, ClientProfile> {
        val clientProfileDocRef = firestore.collection(HOSPITALS_COLLECTION).document(clientId)
        return try {
            val snapshot = clientProfileDocRef.get().await()
            if (snapshot.exists()) {
                snapshot.toObject<ClientProfile>()?.let { Either.Right(it) }
                    ?: Either.Left(ClientProfileFailure.UserNotFound)
            } else {
                Either.Left(ClientProfileFailure.UserNotFound)
            }
        } catch (exception: Exception) {
            Either.Left(
                when (exception) {
                    is FirebaseNetworkException -> ClientProfileFailure.NetworkError(exception)
                    is FirebaseFirestoreException -> ClientProfileFailure.DatabaseError(exception)
                    else -> ClientProfileFailure.UnknownError(exception)
                }
            )
        }
    }

    override suspend fun logout(): Either<LogoutFailure, AuthSuccess> {
        try {
            auth.signOut()
            saveRememberMe(false)
            return Either.Right(AuthSuccess(authenticated = false))
        } catch (e: Exception) {
            return Either.Left(LogoutFailure.UnknownError(e))
        }
    }

    private suspend fun saveRememberMe(rememberMe: Boolean, userId: String? = null) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CLIENT_REMEMBER_ME] = rememberMe
            userId?.let { preferences[PreferencesKeys.CLIENT_ID] = it }
        }
    }
}