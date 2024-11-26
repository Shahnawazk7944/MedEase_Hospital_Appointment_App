package com.example.medeaseclient.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseWrapper @Inject constructor() {
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val authUser: FirebaseAuth = FirebaseAuth.getInstance()
}