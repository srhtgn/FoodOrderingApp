package com.example.foodorderingapp.retrofit

import com.example.foodorderingapp.data.entity.CRUDResponse
import com.example.foodorderingapp.data.entity.FoodsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodsDao {
    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun uploadFoods(): FoodsResponse

    @POST("yemekler/tumYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun search(
        @Field("yemek_adi") searchWord: String
    ): FoodsResponse
}