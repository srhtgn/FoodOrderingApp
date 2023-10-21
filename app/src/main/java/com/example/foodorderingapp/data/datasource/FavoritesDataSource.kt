package com.example.foodorderingapp.data.datasource

import com.example.foodorderingapp.data.entity.Favorites
import com.example.foodorderingapp.room.FavoritesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoritesDataSource(var favDao: FavoritesDao) {
    suspend fun save(yemek_id: Int, yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: String) {
        val newFavorite = Favorites(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat)
        favDao.save(newFavorite)
    }
    suspend fun delete(yemek_id: Int) {
        val deletedFavorite = Favorites(yemek_id, "", "", "")
        favDao.delete(deletedFavorite)
    }

    suspend fun uploadFavorite(): List<Favorites> = withContext(Dispatchers.IO) {
        return@withContext favDao.uploadFavorite()
    }

    suspend fun search(searchWord: String): List<Favorites> = withContext(Dispatchers.IO) {
        return@withContext favDao.search(searchWord)
    }

}