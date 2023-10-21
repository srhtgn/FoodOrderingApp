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
            // Veriyi kaydetmeye çalış
            favDataSource.save(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat)
            return true // Başarılı olduğunda true döndür
        } catch (e: Exception) {
            // Hata oluştuğunda logla
            Log.e("FavoritesRepository", "Veri kaydedilirken hata oluştu: ${e.message}")
            return false // Başarısız olduğunda false döndür
        }
    }


    suspend fun delete(yemek_id: Int) = favDataSource.delete(yemek_id)

    suspend fun uploadFavorite(): List<Favorites> {
        try {
            val result = favDataSource.uploadFavorite()
            return result ?: emptyList() // Eğer null bir sonuç gelirse, boş bir liste döndür
        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Veri yüklenirken hata oluştu: ${e.message}")
            return emptyList() // Hata durumunda boş bir liste döndür
        }
    }

    suspend fun search(searchWord: String): List<Favorites> = favDataSource.search(searchWord)
}