package com.example.medease.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import arrow.core.Either
import com.example.medease.data.firebase.FirebaseWrapper
import com.example.medease.data.util.PreferencesKeys
import com.example.medease.data.util.USERS_COLLECTION
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthRepositoryImpl @Inject constructor(
    private val firebaseWrapper: FirebaseWrapper,
    private val dataStore: DataStore<Preferences>
) : UserAuthRepository {
    private val auth = firebaseWrapper.authUser
    private val firestore = firebaseWrapper.firestore
    override suspend fun userSignUp(
        name: String,
        email: String,
        phone: String,
        password: String,
        rememberMe: Boolean
    ): Either<SignupWithEmailAndPasswordFailure, AuthSuccess> {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null) {
                val clientProfile = UserProfile(
                    name,
                    email,
                    phone,
                    user.photoUrl.toString()
                )
                firestore.collection(USERS_COLLECTION).document(user.uid).set(clientProfile)
                    .await()
                if (rememberMe) saveRememberMe(true, user.uid)
                return Either.Right(AuthSuccess(authenticated = true))
            } else {
                return Either.Left(SignupWithEmailAndPasswordFailure.UnknownError(Exception("User is null")))
            }
        } catch (e: FirebaseAuthException) {
            val failure = when (e) {
                is FirebaseAuthWeakPasswordException -> SignupWithEmailAndPasswordFailure.WeakPassword
                is FirebaseAuthInvalidCredentialsException -> SignupWithEmailAndPasswordFailure.InvalidEmail
                is FirebaseAuthUserCollisionException -> SignupWithEmailAndPasswordFailure.AccountAlreadyExists
                else -> SignupWithEmailAndPasswordFailure.UnknownError(e)
            }
            return Either.Left(failure)
        }
    }

    override suspend fun userSignIn(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Either<SignInWithEmailAndPasswordFailure, AuthSuccess> {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null) {
                if (rememberMe) saveRememberMe(true, user.uid)
                return Either.Right(AuthSuccess(authenticated = true))
            } else {
                return Either.Left(SignInWithEmailAndPasswordFailure.InvalidCredentials)
            }
        } catch (e: FirebaseAuthException) {
            val failure = when (e) {
                is FirebaseAuthInvalidCredentialsException -> SignInWithEmailAndPasswordFailure.InvalidCredentials
                else -> SignInWithEmailAndPasswordFailure.UnknownError(e)
            }
            return Either.Left(failure)
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