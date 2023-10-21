package com.example.foodorderingapp.ui.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.data.entity.Favorites
import com.example.foodorderingapp.data.entity.Foods
import com.example.foodorderingapp.databinding.FoodCardDesignBinding
import com.example.foodorderingapp.ui.fragment.MainPageFragmentDirections
import com.example.foodorderingapp.ui.viewmodel.FavoritesViewModel
import com.example.foodorderingapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderingapp.utils.transition

class FoodsAdapter(
    var mContext: Context,
    var foodList: List<Foods>,
    var viewModel: MainPageViewModel,
) : RecyclerView.Adapter<FoodsAdapter.CardDesignHolder>() {
    val cardSize = 500
    private val sharedPreferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    inner class CardDesignHolder(var design: FoodCardDesignBinding) :
        RecyclerView.ViewHolder(design.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignHolder {
        val binding = FoodCardDesignBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardDesignHolder(binding)
    }

    override fun onBindViewHolder(holder: CardDesignHolder, position: Int) {
        val food = foodList[position]
        val t = holder.design
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}"

        Glide.with(mContext)
            .load(url)
            .override(cardSize)
            .into(t.imageViewFood)
        t.textViewFoodName.text = food.yemek_adi
        t.textViewFoodPrice.text = "${food.yemek_fiyat}₺"

        val isFavorite = sharedPreferences.getBoolean("favorite_${food.yemek_id}", false)
        t.favoriteToggleButton.isChecked = isFavorite

        t.cardFood.setOnClickListener {
            val transition = MainPageFragmentDirections.foodDetailBottomSheetTransition(food)
            Navigation.transition(it, transition)
        }

        t.favoriteToggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().putBoolean("favorite_${food.yemek_id}", isChecked).apply()

            if (isChecked) {
                save(food.yemek_id, food.yemek_adi, food.yemek_resim_adi, "${food.yemek_fiyat}₺")
            } else {
                delete(food.yemek_id)
            }
        }
    }


    override fun getItemCount(): Int {
        return foodList.size
    }

    fun save(yemek_id: Int,yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: String) {
        viewModel.save(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat)
    }

    fun delete(yemek_id: Int) {
        viewModel.delete(yemek_id)
    }
}