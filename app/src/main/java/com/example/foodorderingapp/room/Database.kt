package com.example.foodorderingapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodorderingapp.data.entity.Favorites

@Database(entities = [Favorites::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun getFavoritesDao(): FavoritesDao
}