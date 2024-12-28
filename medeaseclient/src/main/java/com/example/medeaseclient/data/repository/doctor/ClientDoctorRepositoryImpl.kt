package com.example.medeaseclient.data.repository.doctor

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.medeaseclient.data.firebase.FirebaseWrapper
import com.example.medeaseclient.data.util.DOCTORS_COLLECTION
import com.example.medeaseclient.domain.model.Doctor
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientDoctorRepositoryImpl @Inject constructor(
    private val firebaseWrapper: FirebaseWrapper,
) : ClientDoctorRepository {

    private val firestore = firebaseWrapper.firestore
    private val database = firestore.collection(DOCTORS_COLLECTION)

    override suspend fun fetchDoctors(): Flow<Either<DoctorsFailure, List<Doctor>>> {
        return callbackFlow {
            val listenerRegistration = database.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(DoctorsFailure.DatabaseError(error).left())
                    return@addSnapshotListener
                }

                val doctors = snapshot?.toObjects(Doctor::class.java)?.map { doctor ->
                    doctor.copy(doctorId = snapshot.documents.find { it.toObject(Doctor::class.java) == doctor }?.id ?: "")
                }?.toMutableList() ?: mutableListOf()

                snapshot?.documentChanges?.forEach { documentChange ->
                    when (documentChange.type) {
                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                            val doctor = documentChange.document.toObject(Doctor::class.java)
                                .copy(doctorId = documentChange.document.id)
                            doctors.removeAll { it.doctorId == doctor.doctorId } // Prevent duplicates
                            doctors.add(doctor)
                        }
                        DocumentChange.Type.REMOVED -> {
                            val doctorId = documentChange.document.id
                            doctors.removeAll { it.doctorId == doctorId }
                        }
                    }
                }

                trySend(doctors.toList().right()) // Send an immutable list
            }

            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    override suspend fun addDoctor(doctor: Doctor): Either<DoctorsFailure, DoctorsSuccess> {
        return try {
            val documentReference = database.document()
            documentReference.set(doctor.copy(doctorId = documentReference.id)).await()
            DoctorsSuccess.DoctorAdded.right()
        } catch (e: Exception) {
            DoctorsFailure.DatabaseError(e).left()
        }
    }

    override suspend fun updateDoctor(doctor: Doctor): Either<DoctorsFailure, DoctorsSuccess> {
        return try {
            database.document(doctor.doctorId).set(doctor).await()
            DoctorsSuccess.DoctorUpdated.right()
        } catch (e: Exception) {
            DoctorsFailure.DatabaseError(e).left()
        }
    }

    override suspend fun deleteDoctor(doctorId: String): Either<DoctorsFailure, DoctorsSuccess> {
        return try {
            database.document(doctorId).delete().await()
            DoctorsSuccess.DoctorDeleted.right()
        } catch (e: Exception) {
            DoctorsFailure.DatabaseError(e).left()
        }
    }
}