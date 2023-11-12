package com.example.foodorderingapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.data.entity.CartFoods
import com.example.foodorderingapp.databinding.CartCardDesignBinding
import com.example.foodorderingapp.ui.viewmodel.CartViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.integrity.internal.t
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartAdapter(
    var mContext: Context,
    var cartFoodList: List<CartFoods>,
    var viewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CardDesignHolder>() {
    val cardSize = 300

    // Inner class representing the ViewHolder for the RecyclerView
    inner class CardDesignHolder(var design: CartCardDesignBinding) :
        RecyclerView.ViewHolder(design.root)

    // Called when the RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignHolder {
        // Inflate the layout for a card item and create a ViewHolder
        val binding =
            CartCardDesignBinding.inflate(LayoutInflater.from(mContext), parent, false)

        return CardDesignHolder(binding)
    }

    // Called to bind data to a specific ViewHolder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardDesignHolder, position: Int) {
        // Retrieve the data for the current position
        val cartFood = cartFoodList[position]
        val t = holder.design
        val url = cartFood.yemek_resim_adi

        // Load image into the ImageView using Glide library
        Glide.with(mContext)
            .load(url)
            .override(cardSize)
            .into(t.imageViewCartFood)

        // Set food name, price, and quantity text
        t.textViewCartFoodName.text = cartFood.yemek_adi
        t.textViewCartFoodPrice.text = "Price: ${cartFood.yemek_fiyat}₺"
        t.textViewNumber2.text = "Number: ${cartFood.yemek_siparis_adet}"

        // Set click listener for delete button, showing a Snackbar for confirmation
        t.imageViewDelete.setOnClickListener {
            Snackbar.make(it, "Delete ${cartFood.yemek_adi}?", Snackbar.LENGTH_SHORT)
                .setAction("YES") {
                    // Delete operation performed when user confirms
                    delete(cartFood.sepet_yemek_id, cartFood.kullanici_adi)
                }.show()
        }
    }

    // Returns the total number of items in the data set
    override fun getItemCount(): Int {
        return cartFoodList.size
    }

    // Function to initiate the delete operation through the ViewModel
    fun delete(sepet_yemek_id: Int, kullanici_adi: String) {
        viewModel.delete(sepet_yemek_id, kullanici_adi)
    }
}