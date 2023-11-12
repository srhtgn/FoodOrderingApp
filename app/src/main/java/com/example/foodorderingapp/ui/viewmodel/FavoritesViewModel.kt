package com.example.foodorderingapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderingapp.data.entity.Favorites
import com.example.foodorderingapp.data.repository.firebase.AuthRepository
import com.example.foodorderingapp.data.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(var favoritesRepository: FavoritesRepository, val authRepository: AuthRepository) : ViewModel() {
    // LiveData to hold the list of favorite items
    var favoritesList = MutableLiveData<List<Favorites>>()

    // Initialize the ViewModel by uploading the user's favorite items
    init {
        uploadFavorites()
    }

    // Coroutine function to upload the user's favorite items
    fun uploadFavorites() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Attempt to upload the favorite items and update the LiveData
                favoritesList.value = favoritesRepository.uploadFavorite()
            } catch (e: Exception) {
                // Log any exceptions that may occur during the upload
                Log.e("Error", "Error in uploading favorites: ${e.message}", e)
            }
        }
    }

    // Coroutine function to delete a favorite item
    fun delete(foodId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            // Delete the favorite item from the repository and upload the updated list
            favoritesRepository.delete(foodId)
            uploadFavorites()
        }
    }

    // Coroutine function to search for favorite items based on a search word
    fun search(searchWord: String) {
        CoroutineScope(Dispatchers.Main).launch {
            // Perform a search and update the LiveData with the results
            favoritesList.value = favoritesRepository.search(searchWord)
        }
    }
}