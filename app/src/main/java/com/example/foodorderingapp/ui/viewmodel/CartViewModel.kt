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

    // LiveData to hold the list of items in the cart
    var cartFoodList = MutableLiveData<List<CartFoods>>()

    // Initialize the ViewModel
    init {
        // Observe changes in the current user data
        val currentUserLiveData = authRepository.getCurrentUserLiveData()
        currentUserLiveData.observeForever { firebaseUser ->
            if (firebaseUser != null) {
                // If the user is authenticated, fetch their email
                val kullaniciEmail = firebaseUser.email
                if (kullaniciEmail != null) {
                    // Fetch the username and upload cart foods for the user
                    fetchUsernameAndUploadCartFoods(kullaniciEmail)
                }
            }
        }
    }

    // Fetch the username based on the user's email and upload cart foods
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
                        // Upload cart foods for the user and log the username
                        uploadCartFoods(kullanici_adi)
                        Log.e("CartViewModel", kullanici_adi)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle the failure of the Firestore query
            }
    }

    // Delete an item from the cart
    fun delete(sepet_yemek_id: Int, kullanici_adi: String) {
        CoroutineScope(Dispatchers.Main).launch {
            // Delete the item from the cart repository and upload updated cart foods
            cRepo.delete(sepet_yemek_id, kullanici_adi)
            uploadCartFoods(kullanici_adi)
        }
    }

    // Upload the updated cart foods for the user
    fun uploadCartFoods(kullanici_adi: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Attempt to upload the updated cart foods and update the LiveData
                cartFoodList.value = cRepo.uploadCartFoods(kullanici_adi)
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the upload
            }
        }
    }

}