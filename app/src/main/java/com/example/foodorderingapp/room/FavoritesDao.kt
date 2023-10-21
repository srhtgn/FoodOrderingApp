package com.example.foodorderingapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.foodorderingapp.data.entity.Favorites

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    suspend fun uploadFavorite(): List<Favorites>

    @Insert
    suspend fun save(favorite: Favorites)

    @Delete
    suspend fun delete(favorite: Favorites)

    @Query("SELECT * FROM favorites WHERE yemek_adi like '%' || :searchWord || '%'")
    suspend fun search(searchWord: String): List<Favorites>
}