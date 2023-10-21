package com.example.foodorderingapp.data.datasource

import android.util.Log
import com.example.foodorderingapp.data.entity.Foods
import com.example.foodorderingapp.retrofit.FoodsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodsDataSource (var fDao: FoodsDao) {

    suspend fun uploadFoods(): List<Foods> = withContext(Dispatchers.IO) {
        return@withContext fDao.uploadFoods().yemekler
    }

    suspend fun search(searchWord : String): List<Foods> = withContext(Dispatchers.IO) {
        val response = fDao.search(searchWord)
        return@withContext response.yemekler
    }
}