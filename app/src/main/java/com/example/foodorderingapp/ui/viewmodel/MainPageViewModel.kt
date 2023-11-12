package com.example.foodorderingapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.util.query
import com.example.foodorderingapp.data.entity.Favorites
import com.example.foodorderingapp.data.entity.Foods
import com.example.foodorderingapp.data.repository.FavoritesRepository
import com.example.foodorderingapp.data.repository.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    var foodsRepository: FoodsRepository,
    var favoritesRepository: FavoritesRepository
) : ViewModel() {

    // LiveData to hold the list of all foods and the list of foods for search results
    var foodList = MutableLiveData<List<Foods>>()
    var searchFoods = MutableLiveData<List<Foods>>()

    // Initialize the ViewModel by uploading the list of foods
    init {
        uploadFoods()
    }

    // Coroutine function to upload the list of foods
    fun uploadFoods() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Attempt to upload the list of foods and update the LiveData
                foodList.value = foodsRepository.uploadFoods()
                searchFoods.value = foodsRepository.uploadFoods()
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the upload
            }
        }
    }

    // Coroutine function to search for foods based on a search word
    fun search(searchWord: String) {
        // Get the list of all foods
        val foods = foodList.value ?: emptyList()

        // Filter foods based on the search word and update the search results LiveData
        val searchResponse = foods.filter { food ->
            food.yemek_adi.contains(searchWord, ignoreCase = true)
        }
        searchFoods.value = searchResponse
    }

    // Coroutine function to save a food item to favorites
    fun save(
        foodId: Int,
        foodName: String,
        foodImageName: String,
        foodPrice: String,
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Attempt to save the food item to favorites
                favoritesRepository.save(foodId, foodName, foodImageName, foodPrice)
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the save operation
            }
        }
    }

    // Coroutine function to delete a food item from favorites
    fun delete(foodId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Attempt to delete the food item from favorites
                favoritesRepository.delete(foodId)
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the delete operation
            }
        }
    }
}
