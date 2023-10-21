package com.example.foodorderingapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.data.entity.CartFoods
import com.example.foodorderingapp.databinding.CartCardDesignBinding
import com.example.foodorderingapp.ui.viewmodel.CartViewModel
import com.google.android.material.snackbar.Snackbar

class CartAdapter(
    var mContext: Context,
    var cartFoodList: List<CartFoods>,
    var viewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CardDesignHolder>() {
    val cardSize = 300

    inner class CardDesignHolder(var design: CartCardDesignBinding) :
        RecyclerView.ViewHolder(design.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignHolder {
        val binding =
            CartCardDesignBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardDesignHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardDesignHolder, position: Int) {
        val cartFood = cartFoodList[position]
        val t = holder.design
        val url = cartFood.yemek_resim_adi

        Glide.with(mContext)
            .load(url)
            .override(cardSize)
            .into(t.imageViewCartFood)
        t.textViewCartFoodName.text = cartFood.yemek_adi
        t.textViewCartFoodPrice.text = "Fiyat: ${cartFood.yemek_fiyat}₺"
        t.textViewNumber2.text = "Adet: ${cartFood.yemek_siparis_adet}"

        t.imageViewDelete.setOnClickListener {
            Snackbar.make(it, "${cartFood.yemek_adi} silinsin mi?", Snackbar.LENGTH_SHORT)
                .setAction("EVET") {
                    delete(cartFood.sepet_yemek_id, cartFood.kullanici_adi)
                }.show()
        }
    }

    override fun getItemCount(): Int {
        return cartFoodList.size
    }

    fun delete(sepet_yemek_id: Int, kullanici_adi: String) {
        viewModel.delete(sepet_yemek_id, kullanici_adi)
        notifyDataSetChanged() // Tüm veriyi güncelle
    }
}