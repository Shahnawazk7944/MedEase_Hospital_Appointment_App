package com.example.medeaseclient.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import arrow.core.Either
import com.example.medeaseclient.data.firebase.FirebaseWrapper
import com.example.medeaseclient.data.util.HOSPITALS_COLLECTION
import com.example.medeaseclient.data.util.PreferencesKeys
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientAuthRepositoryImpl @Inject constructor(
    private val firebaseWrapper: FirebaseWrapper,
    private val dataStore: DataStore<Preferences>
) : ClientAuthRepository {
    private val auth = firebaseWrapper.authUser
    private val firestore = firebaseWrapper.firestore
    override suspend fun clientSignUp(
        hospitalName: String,
        hospitalEmail: String,
        hospitalPhone: String,
        hospitalCity: String,
        hospitalPinCode: String,
        password: String,
        rememberMe: Boolean
    ): Either<SignupWithEmailAndPasswordFailure, AuthSuccess> {
        try {
            Log.e("ClientAuthRepositoryImpl", "clientSignUp: $hospitalEmail $password")
            val authResult = auth.createUserWithEmailAndPassword(hospitalEmail, password).await()
            val user = authResult.user
            if (user != null) {
                val clientProfile = ClientProfile(
                    hospitalName,
                    hospitalEmail,
                    hospitalPhone,
                    hospitalCity,
                    hospitalPinCode,
                    user.photoUrl.toString()
                )
                firestore.collection(HOSPITALS_COLLECTION).document(user.uid).set(clientProfile).await()
                if (rememberMe) saveRememberMe(true, user.uid)
                return Either.Right(AuthSuccess(authenticated = true))
            } else {
                return Either.Left(SignupWithEmailAndPasswordFailure.UnknownError(Exception("User is null")))
            }
        } catch (e: FirebaseAuthException) {
            Log.e("ClientAuthRepositoryImpl", "clientSignUp: ${e.message}")
            val failure = when (e) {
                is FirebaseAuthWeakPasswordException -> SignupWithEmailAndPasswordFailure.WeakPassword
                is FirebaseAuthInvalidCredentialsException -> SignupWithEmailAndPasswordFailure.InvalidEmail
                is FirebaseAuthUserCollisionException -> SignupWithEmailAndPasswordFailure.AccountAlreadyExists
                else -> SignupWithEmailAndPasswordFailure.UnknownError(e)
            }
            return Either.Left(failure)
        }
    }

    override suspend fun clientSignIn(
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
            Log.e("ClientAuthRepositoryImpl", "clientSignIn: ${e.message}")
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
            preferences[PreferencesKeys.CLIENT_REMEMBER_ME] = rememberMe
            userId?.let { preferences[PreferencesKeys.CLIENT_ID] = it }
        }
    }
}