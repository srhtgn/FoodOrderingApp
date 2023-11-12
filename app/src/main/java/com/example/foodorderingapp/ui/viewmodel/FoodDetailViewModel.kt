package com.example.foodorderingapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
class FoodDetailViewModel @Inject constructor(
    var cartRepository: CartRepository,
    val authRepository: AuthRepository
) : ViewModel() {
    // LiveData to hold the list of items in the cart
    var cartFoodList = MutableLiveData<List<CartFoods>>()

    // Initialize the ViewModel
    init {
        // Observe changes in the current user data
        val currentUserLiveData = authRepository.getCurrentUserLiveData()
        currentUserLiveData.observeForever { firebaseUser ->
            if (firebaseUser != null) {
                // If the user is authenticated, fetch their email
                val userEmail = firebaseUser.email
                if (userEmail != null) {
                    // Fetch the username and upload cart foods for the user
                    fetchUsernameAndUploadFoodDetail(userEmail)
                }
            }
        }
    }

    // Fetch the username based on the user's email and upload cart foods
    private fun fetchUsernameAndUploadFoodDetail(userEmail: String) {
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore.collection("users")

        usersCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userDocument = querySnapshot.documents[0]
                    val username = userDocument.getString("username")
                    if (username != null) {
                        // Upload cart foods for the user and log the username
                        uploadCartFoods(username)
                        Log.e("CartViewModel", username)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle the failure of the Firestore query
            }
    }

    // Coroutine function to upload the updated cart foods for the user
    fun uploadCartFoods(username: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Attempt to upload the updated cart foods and update the LiveData
                cartFoodList.value = cartRepository.uploadCartFoods(username)
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the upload
            }
        }
    }

    // Coroutine function to save a new item to the cart
    fun save(
        foodName: String,
        foodImageName: String,
        foodPrice: Int,
        foodOrderQuantity: Int,
        username: String
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Attempt to save the item to the cart repository
                cartRepository.save(
                    foodName,
                    foodImageName,
                    foodPrice,
                    foodOrderQuantity,
                    username
                )
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the save operation
            }
        }
    }

    // Coroutine function to delete an item from the cart
    fun delete(cartItemId: Int, username: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Attempt to delete the item from the cart repository
                cartRepository.delete(cartItemId, username)
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the delete operation
            }
            // Upload the updated cart foods after deletion
            uploadCartFoods(username)
        }
    }
}