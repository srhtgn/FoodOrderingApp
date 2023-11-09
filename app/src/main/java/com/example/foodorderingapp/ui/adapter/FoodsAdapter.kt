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
    var mContext: Context, // Bağlam nesnesi
    var foodList: List<Foods>, // Yemek listesi
    var viewModel: MainPageViewModel, // Ana sayfa görünüm modeli
) : RecyclerView.Adapter<FoodsAdapter.CardDesignHolder>() {

    val cardSize = 500 // Kart boyutu
    private val sharedPreferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    inner class CardDesignHolder(var design: FoodCardDesignBinding) :
        RecyclerView.ViewHolder(design.root) // Kart tasarımı tutan iç içe sınıf

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignHolder {
        // Kart görünümünü oluştur
        val binding = FoodCardDesignBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardDesignHolder(binding)
    }

    override fun onBindViewHolder(holder: CardDesignHolder, position: Int) {
        val food = foodList[position] // Belirli bir konum için yemek öğesi
        val t = holder.design // Kart tasarımı

        // Yemek resminin URL'si
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}"

        // Glide ile resmi yükle
        Glide.with(mContext)
            .load(url)
            .override(cardSize)
            .into(t.imageViewFood)

        // Yemek adını ve fiyatını ayarla
        t.textViewFoodName.text = food.yemek_adi
        t.textViewFoodPrice.text = "${food.yemek_fiyat}₺"

        // Yemek favori mi kontrol et
        val isFavorite = sharedPreferences.getBoolean("favorite_${food.yemek_id}", false)
        t.favoriteToggleButton.isChecked = isFavorite

        // Kart tıklanınca ayrıntılarını göster
        t.cardFood.setOnClickListener {
            val transition = MainPageFragmentDirections.foodDetailBottomSheetTransition(food)
            Navigation.transition(it, transition)
        }

        // Favori düğmesinin durum değişikliğini dinle
        t.favoriteToggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            // Tercihlere favori durumunu kaydet
            sharedPreferences.edit().putBoolean("favorite_${food.yemek_id}", isChecked).apply()

            // Eğer seçiliyse, yemeği kaydet; değilse sil
            if (isChecked) {
                save(food.yemek_id, food.yemek_adi, food.yemek_resim_adi, "${food.yemek_fiyat}₺")
            } else {
                delete(food.yemek_id)
            }
        }
    }

    fun updateData(newData: List<Foods>) {
        foodList = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return foodList.size // Yemek listesinin boyutunu döndür
    }

    // Yemeği kaydetme işlemi
    fun save(yemek_id: Int, yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: String) {
        viewModel.save(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat)
    }

    // Yemeği silme işlemi
    fun delete(yemek_id: Int) {
        viewModel.delete(yemek_id)
    }
}
