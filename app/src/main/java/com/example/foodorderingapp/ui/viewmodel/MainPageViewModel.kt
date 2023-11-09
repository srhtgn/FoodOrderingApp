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
    var fRepo: FoodsRepository,
    var favRepo: FavoritesRepository
) : ViewModel() {

    var foodList = MutableLiveData<List<Foods>>()
    var searchFoods = MutableLiveData<List<Foods>>()

    init {
        uploadFoods()
    }

    //Foods
    fun uploadFoods() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                foodList.value = fRepo.uploadFoods()
                searchFoods.value = fRepo.uploadFoods()
            } catch (e: Exception) {
            }
        }
    }

    fun search(searchWord: String) {
        val foods = foodList.value ?: emptyList()
        val searchResponse =
            foods.filter { foods ->
                foods.yemek_adi.contains(searchWord, ignoreCase = true)
            }
        searchFoods.value = searchResponse
    }

    //Favorites

    fun save(
        yemek_id: Int,
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: String,
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                favRepo.save(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat)
            } catch (e: Exception) {
            }
        }
    }

    fun delete(yemek_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            favRepo.delete(yemek_id)
        }
    }
}