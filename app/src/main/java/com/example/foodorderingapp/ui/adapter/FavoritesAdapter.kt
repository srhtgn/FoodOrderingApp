package com.example.foodorderingapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.data.entity.Favorites
import com.example.foodorderingapp.databinding.FavoritesCardDesignBinding
import com.example.foodorderingapp.ui.fragment.FavoritesFragmentDirections
import com.example.foodorderingapp.ui.fragment.MainPageFragmentDirections
import com.example.foodorderingapp.ui.viewmodel.FavoritesViewModel
import com.example.foodorderingapp.utils.transition
import com.google.android.material.snackbar.Snackbar

class FavoritesAdapter(
    private val mContext: Context,
    private val favoritesList: List<Favorites>,
    private val viewModel: FavoritesViewModel
) : RecyclerView.Adapter<FavoritesAdapter.CardDesignHolder>() {

    // Size for image loading
    private val cardSize = 500

    // Inner class representing the ViewHolder for the RecyclerView
    inner class CardDesignHolder(val design: FavoritesCardDesignBinding) :
        RecyclerView.ViewHolder(design.root)

    // Called when the RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignHolder {
        // Inflate the layout for a card item and create a ViewHolder
        val binding = FavoritesCardDesignBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardDesignHolder(binding)
    }

    // Called to bind data to a specific ViewHolder
    override fun onBindViewHolder(holder: CardDesignHolder, position: Int) {
        // Retrieve the data for the current position
        val favorite = favoritesList[position]
        val cardDesign = holder.design
        val imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${favorite.yemek_resim_adi}"

        // Load image into the ImageView using Glide library
        Glide.with(mContext)
            .load(imageUrl)
            .override(cardSize)
            .into(cardDesign.imageViewFavoritesFood)

        // Set food name and price text
        cardDesign.textViewFavoritesFoodName.text = favorite.yemek_adi
        cardDesign.textViewFavoritesFoodPrice.text = favorite.yemek_fiyat

        // Log favorite details for debugging
        Log.e("FavoritesAdapter", favorite.toString())
        Log.e("Favorite", favorite.yemek_adi)
    }

    // Returns the total number of items in the data set
    override fun getItemCount(): Int {
        return favoritesList.size
    }
}
