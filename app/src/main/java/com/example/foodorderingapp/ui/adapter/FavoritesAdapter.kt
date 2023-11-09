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
    var mContext: Context,
    var favoritesList: List<Favorites>,
    var viewModel: FavoritesViewModel
) : RecyclerView.Adapter<FavoritesAdapter.CardDesignHolder>() {
    val cardSize = 500

    inner class CardDesignHolder(var design: FavoritesCardDesignBinding) :
        RecyclerView.ViewHolder(design.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesAdapter.CardDesignHolder {
        val binding = FavoritesCardDesignBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardDesignHolder(binding)
    }

    override fun onBindViewHolder(holder: CardDesignHolder, position: Int) {
        val favorite = favoritesList[position]
        val t = holder.design
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${favorite.yemek_resim_adi}"

        Log.e("FavoritesAdapter", favorite.toString())

        Log.e("Favori", favorite.yemek_adi)

        Glide.with(mContext)
            .load(url)
            .override(cardSize)
            .into(t.imageViewFavoritesFood)
        t.textViewFavoritesFoodName.text = favorite.yemek_adi
        t.textViewFavoritesFoodPrice.text = favorite.yemek_fiyat

    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }
}