package com.example.foodorderingapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.foodorderingapp.data.datasource.CartDataSource
import com.example.foodorderingapp.data.entity.CartFoods
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class CartRepository @Inject constructor(var cds: CartDataSource) {

    suspend fun save(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) =
        cds.save(
            yemek_adi,
            yemek_resim_adi,
            yemek_fiyat,
            yemek_siparis_adet,
            kullanici_adi
        )

    suspend fun delete(sepet_yemek_id: Int, kullanici_adi: String) =
        cds.delete(sepet_yemek_id, kullanici_adi)

    suspend fun uploadCartFoods(kullanici_adi: String): List<CartFoods> =
        cds.uploadCartFoods(kullanici_adi)
}