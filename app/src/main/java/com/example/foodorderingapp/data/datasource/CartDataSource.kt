package com.example.foodorderingapp.data.datasource

import android.util.Log
import com.example.foodorderingapp.data.entity.CartFoods
import com.example.foodorderingapp.retrofit.CartDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartDataSource(var cDao: CartDao) {

    suspend fun save(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) {
        val response =
            cDao.save(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)

        Log.e("Kaydet", "${response.message} - ${response.success}")
    }

    suspend fun delete(sepet_yemek_id: Int, kullanici_adi: String) {
        val response = cDao.delete(sepet_yemek_id, kullanici_adi)
        Log.e("Kişi Sil", "Başarı: ${response.success} - Mesaj: ${response.message}")
    }

    suspend fun uploadCartFoods(kullanici_adi: String): List<CartFoods> =
        withContext(Dispatchers.IO) {

            val response = cDao.uploadCartFoods(kullanici_adi)
            return@withContext response.sepet_yemekler
        }
}