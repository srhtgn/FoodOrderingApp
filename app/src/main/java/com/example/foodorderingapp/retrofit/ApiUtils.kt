package com.example.foodorderingapp.retrofit

class ApiUtils {
    companion object {
        val BASE_URL = "http://kasimadalan.pe.hu/"

        fun getFoodsDao(): FoodsDao {
            return RetrofitClient.getClient(BASE_URL).create(FoodsDao::class.java)
        }

        fun getCartDao(): CartDao {
            return RetrofitClient.getClient(BASE_URL).create(CartDao::class.java)
        }
    }
}