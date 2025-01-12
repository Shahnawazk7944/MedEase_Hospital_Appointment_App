package com.example.medease.data.repository.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.medease.data.firebase.FirebaseWrapper
import com.example.medease.data.repository.auth.AuthSuccess
import com.example.medease.data.util.BEDS_COLLECTION
import com.example.medease.data.util.DOCTORS_COLLECTION
import com.example.medease.data.util.HOSPITALS_COLLECTION
import com.example.medease.data.util.PreferencesKeys
import com.example.medease.data.util.USERS_COLLECTION
import com.example.medease.domain.model.Bed
import com.example.medease.domain.model.Doctor
import com.example.medease.domain.model.HospitalWithDoctors
import com.example.medease.domain.model.UserProfile
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
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

    override suspend fun fetchDoctorsWithHospitals(): Flow<Either<UserOperationsFailure, List<HospitalWithDoctors>>> {
        return firestore.collection(DOCTORS_COLLECTION)
            .addSnapshotListenerFlow<Doctor>()
            .map { either ->
                either.fold(
                    { failure -> Either.Left(failure) },
                    { querySnapshot ->
                        val doctors = querySnapshot.toObjects(Doctor::class.java)
                        val hospitalIds = doctors.map { it.hospitalId }.distinct()

                        val hospitalsWithDoctors = hospitalIds.mapNotNull { hospitalId ->
                            val hospitalDoc =
                                firestore.collection(HOSPITALS_COLLECTION).document(hospitalId)
                                    .get().await()
                            val hospitalData = hospitalDoc.toObject<HospitalWithDoctors>()
                            hospitalData?.copy(doctors = doctors.filter { it.hospitalId == hospitalId })
                        }
                        Either.Right(hospitalsWithDoctors)
                    })
            }
    }

    override suspend fun fetchBedsFromHospital(hospitalId: String): Flow<Either<UserOperationsFailure, List<Bed>>> {
        return callbackFlow {
            val bedsCollection = firestore.collection(HOSPITALS_COLLECTION).document(hospitalId)
                .collection(BEDS_COLLECTION)
            val listenerRegistration = bedsCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(UserOperationsFailure.DatabaseError(error).left())
                    return@addSnapshotListener
                }
                val beds = snapshot?.toObjects(Bed::class.java)?.map { bed ->
                    bed.copy(
                        bedId = snapshot.documents.find { it.toObject(Bed::class.java) == bed }?.id
                            ?: ""
                    )
                }?.toMutableList() ?: mutableListOf()

                snapshot?.documentChanges?.forEach { documentChange ->
                    when (documentChange.type) {
                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                            val bed = documentChange.document.toObject(Bed::class.java)
                                .copy(bedId = documentChange.document.id)
                            beds.removeAll { it.bedId == bed.bedId }
                            beds.add(bed)
                        }

                        DocumentChange.Type.REMOVED -> {
                            val bedId = documentChange.document.id
                            beds.removeAll { it.bedId == bedId }
                        }
                    }
                }
                trySend(beds.right())
            }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    private inline fun <reified T> CollectionReference.addSnapshotListenerFlow(): Flow<Either<UserOperationsFailure, QuerySnapshot>> =
        callbackFlow {
            val snapshotListener = addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Either.Left(UserOperationsFailure.DatabaseError(error)))
                    close(error)
                    return@addSnapshotListener
                }
                value?.let { trySend(Either.Right(it)) }
                    ?: close(FirebaseException("Snapshot is null"))
            }
            awaitClose { snapshotListener.remove() }
        }

    private suspend fun saveRememberMe(rememberMe: Boolean, userId: String? = null) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_REMEMBER_ME] = rememberMe
            userId?.let { preferences[PreferencesKeys.USER_ID] = it }
        }
    }
}