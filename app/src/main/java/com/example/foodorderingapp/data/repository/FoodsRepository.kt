package com.example.foodorderingapp.data.repository

import com.example.foodorderingapp.data.datasource.FoodsDataSource
import com.example.foodorderingapp.data.entity.Foods
import javax.inject.Inject

class FoodsRepository @Inject constructor(var fds: FoodsDataSource) {

    suspend fun uploadFoods(): List<Foods> = fds.uploadFoods()

    suspend fun search(searchWord: String): List<Foods> = fds.search(searchWord)
}