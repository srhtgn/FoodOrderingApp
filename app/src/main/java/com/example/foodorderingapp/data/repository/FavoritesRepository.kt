package com.example.foodorderingapp.data.repository

import android.util.Log
import com.example.foodorderingapp.data.datasource.FavoritesDataSource
import com.example.foodorderingapp.data.entity.Favorites

class FavoritesRepository(var favDataSource: FavoritesDataSource) {
    suspend fun save(
        yemek_id: Int,
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: String,
    ): Boolean {
        try {
            favDataSource.save(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat)
            return true
        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Veri kaydedilirken hata oluştu: ${e.message}")
            return false
        }
    }


    suspend fun delete(yemek_id: Int) = favDataSource.delete(yemek_id)

    suspend fun uploadFavorite(): List<Favorites> {
        try {
            val result = favDataSource.uploadFavorite()
            return result ?: emptyList()
        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Veri yüklenirken hata oluştu: ${e.message}")
            return emptyList()
        }
    }

    suspend fun search(searchWord: String): List<Favorites> = favDataSource.search(searchWord)
}