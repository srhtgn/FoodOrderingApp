package com.example.foodorderingapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
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
    private val mContext: Context, // Context object
    private var foodList: List<Foods>, // List of food items
    private val viewModel: MainPageViewModel // Main page view model
) : RecyclerView.Adapter<FoodsAdapter.CardDesignHolder>() {

    private val cardSize = 500 // Card size
    private val sharedPreferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    inner class CardDesignHolder(var design: FoodCardDesignBinding) :
        RecyclerView.ViewHolder(design.root) // Inner class holding the card design

    // Called when the RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignHolder {
        // Inflate the layout for a card item and create a ViewHolder
        val binding = FoodCardDesignBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardDesignHolder(binding)
    }

    // Called to bind data to a specific ViewHolder
    override fun onBindViewHolder(holder: CardDesignHolder, position: Int) {
        val food = foodList[position] // Food item for a specific position
        val t = holder.design // Card design

        // URL for the food image
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}"

        // Load image into the ImageView using Glide library
        Glide.with(mContext)
            .load(url)
            .override(cardSize)
            .into(t.imageViewFood)

        // Set food name and price
        t.textViewFoodName.text = food.yemek_adi
        t.textViewFoodPrice.text = "${food.yemek_fiyat}₺"

        // Check if the food is a favorite
        val isFavorite = sharedPreferences.getBoolean("favorite_${food.yemek_id}", false)
        t.favoriteToggleButton.isChecked = isFavorite

        // Show details when the card is clicked
        t.cardFood.setOnClickListener {
            val transition = MainPageFragmentDirections.foodDetailBottomSheetTransition(food)
            Navigation.transition(it, transition)
        }

        // Listen for changes in the favorite toggle button state
        t.favoriteToggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            // Save the favorite status to preferences
            sharedPreferences.edit().putBoolean("favorite_${food.yemek_id}", isChecked).apply()

            // If selected, save the food; otherwise, delete it
            if (isChecked) {
                save(food.yemek_id, food.yemek_adi, food.yemek_resim_adi, "${food.yemek_fiyat}₺")
            } else {
                delete(food.yemek_id)
            }
        }
    }

    // Function to update the data set with new data
    fun updateData(newData: List<Foods>) {
        foodList = newData
        notifyDataSetChanged()
    }

    // Returns the total number of items in the data set
    override fun getItemCount(): Int {
        return foodList.size
    }

    // Save the food item
    fun save(yemek_id: Int, yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: String) {
        viewModel.save(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat)
    }

    // Delete the food item
    fun delete(yemek_id: Int) {
        viewModel.delete(yemek_id)
    }
}