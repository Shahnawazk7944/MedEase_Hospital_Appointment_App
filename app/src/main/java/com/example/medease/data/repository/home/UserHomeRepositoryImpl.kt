package com.example.medease.data.repository.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import arrow.core.Either
import com.example.medease.data.firebase.FirebaseWrapper
import com.example.medease.data.repository.auth.AuthSuccess
import com.example.medease.data.util.PreferencesKeys
import com.example.medease.data.util.USERS_COLLECTION
import com.example.medease.domain.model.UserProfile
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserHomeRepositoryImpl @Inject constructor(
    private val firebaseWrapper: FirebaseWrapper,
    private val dataStore: DataStore<Preferences>
) : UserHomeRepository {
    private val auth = firebaseWrapper.authUser
    private val firestore = firebaseWrapper.firestore

    override suspend fun getUserProfile(userId: String): Either<UserProfileFailure, UserProfile> {
        val userProfileDocRef = firestore.collection(USERS_COLLECTION).document(userId)
        return try {
            val snapshot = userProfileDocRef.get().await()
            if (snapshot.exists()) {
                snapshot.toObject<UserProfile>()?.let { Either.Right(it) }
                    ?: Either.Left(UserProfileFailure.UserNotFound)
            } else {
                Either.Left(UserProfileFailure.UserNotFound)
            }
        } catch (exception: Exception) {
            Either.Left(
                when (exception) {
                    is FirebaseNetworkException -> UserProfileFailure.NetworkError(exception)
                    is FirebaseFirestoreException -> UserProfileFailure.DatabaseError(exception)
                    else -> UserProfileFailure.UnknownError(exception)
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
            preferences[PreferencesKeys.USER_REMEMBER_ME] = rememberMe
            userId?.let { preferences[PreferencesKeys.USER_ID] = it }
        }
    }
}