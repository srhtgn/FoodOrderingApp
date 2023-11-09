package com.example.foodorderingapp.data.repository.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class AuthRepository @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    fun getCurrentUser() = auth.currentUser

    fun signInWithEmailAndPassword(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password)

    fun createUserWithEmailAndPassword(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password)

    fun saveUserData(uid: String, username: String, email: String) =
        FirebaseFirestore.getInstance().collection("users").document(uid)
            .set(hashMapOf("username" to username, "email" to email))

    fun getCurrentUserLiveData(): LiveData<FirebaseUser?> {
        return MutableLiveData(auth.currentUser)
    }

    suspend fun fetchUserDetails(uid: String): String? {
        try {
            val userDoc = firestore.collection("users").document(uid).get().await()
            if (userDoc.exists()) {
                return userDoc.getString("username") // Kullanıcı adını döndür.
            }
        } catch (e: Exception) {
        }
        return null
    }
}

