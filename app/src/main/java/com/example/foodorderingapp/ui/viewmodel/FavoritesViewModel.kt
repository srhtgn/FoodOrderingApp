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
class FavoritesViewModel @Inject constructor(var favRepo: FavoritesRepository, val authRepository: AuthRepository) : ViewModel() {
    var favoritesList = MutableLiveData<List<Favorites>>()

    init {
        uploadFavorites()
    }

    fun uploadFavorites() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                favoritesList.value = favRepo.uploadFavorite()

            }catch (e: Exception){
                Log.e("Hata", "hata liste ${e.message}", e)
            }
        }
    }

    fun delete(yemek_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            favRepo.delete(yemek_id)
            uploadFavorites()
        }
    }

    fun search(searchWord: String) {
        CoroutineScope(Dispatchers.Main).launch {
            favoritesList.value = favRepo.search(searchWord)
        }
    }
}