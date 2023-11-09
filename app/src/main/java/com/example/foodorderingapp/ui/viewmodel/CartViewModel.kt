package com.example.foodorderingapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderingapp.data.entity.CartFoods
import com.example.foodorderingapp.data.repository.firebase.AuthRepository
import com.example.foodorderingapp.data.repository.CartRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(var cRepo: CartRepository, val authRepository: AuthRepository) : ViewModel() {

    var cartFoodList = MutableLiveData<List<CartFoods>>()

    init {
        val currentUserLiveData = authRepository.getCurrentUserLiveData()
        currentUserLiveData.observeForever { firebaseUser ->
            if (firebaseUser != null) {
                val kullaniciEmail = firebaseUser.email
                if (kullaniciEmail != null) {
                    fetchUsernameAndUploadCartFoods(kullaniciEmail)
                }
            }
        }
    }

    fun fetchUsernameAndUploadCartFoods(kullaniciEmail: String) {
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore.collection("users")

        usersCollection.whereEqualTo("email", kullaniciEmail)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userDocument = querySnapshot.documents[0]
                    val kullanici_adi = userDocument.getString("username")
                    if (kullanici_adi != null) {
                        uploadCartFoods(kullanici_adi)
                        Log.e("CartViewModel", kullanici_adi)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Firestore query failed
            }
    }

    fun delete(sepet_yemek_id: Int, kullanici_adi: String) {
        CoroutineScope(Dispatchers.Main).launch {
            cRepo.delete(sepet_yemek_id, kullanici_adi)
            uploadCartFoods(kullanici_adi)
        }
    }

    fun uploadCartFoods(kullanici_adi: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                cartFoodList.value = cRepo.uploadCartFoods(kullanici_adi)
            } catch (e: Exception) {
            }
        }
    }

}