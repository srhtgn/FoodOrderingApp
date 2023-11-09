package com.example.foodorderingapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodorderingapp.R
import com.example.foodorderingapp.data.entity.CartFoods
import com.example.foodorderingapp.databinding.FragmentFoodDetailBottomSheetBinding
import com.example.foodorderingapp.ui.adapter.CartAdapter
import com.example.foodorderingapp.ui.adapter.FoodsAdapter
import com.example.foodorderingapp.ui.viewmodel.CartViewModel
import com.example.foodorderingapp.ui.viewmodel.FoodDetailViewModel
import com.example.foodorderingapp.ui.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class FoodDetailBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFoodDetailBottomSheetBinding
    private lateinit var viewModel: FoodDetailViewModel

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    var totalFoodPrice = 0
    var yemek_siparis_adedi = ""
    var number = 1
    var userName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodDetailBottomSheetBinding.inflate(inflater, container, false)

        val currentUser = firebaseAuth.currentUser

        val bundle: FoodDetailBottomSheetFragmentArgs by navArgs()
        val foodComing = bundle.food
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${foodComing.yemek_resim_adi}"

        Glide.with(this)
            .load(url)
            .override(500)
            .into(binding.imageViewFood2)

        binding.textViewFoodName2.setText(foodComing.yemek_adi)
        binding.textViewFoodPrice2.setText("${foodComing.yemek_fiyat}₺")
        binding.textViewTotalPrice.setText("Price: ${foodComing.yemek_fiyat}₺")


        binding.buttonPlus.setOnClickListener {
            number++
            yemek_siparis_adedi = number.toString()
            binding.textViewNumber.setText(yemek_siparis_adedi)
            binding.textViewTotalPrice.text = "Price: ${number * foodComing.yemek_fiyat}₺"
        }

        binding.buttonMinus.setOnClickListener {
            if (number > 1) {
                number--
                yemek_siparis_adedi = number.toString()
                binding.textViewNumber.setText(yemek_siparis_adedi)
                binding.textViewTotalPrice.text = "Price: ${number * foodComing.yemek_fiyat}₺"
            }
        }


        if (currentUser != null) {
            // Firestore'daki "users" koleksiyonundan kullanıcı adını al
            val uid = currentUser.uid
            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Firestore'dan gelen kullanıcı adını textView'e ayarla
                        val username = documentSnapshot.getString("username")
                        if (username != null) {
                            userName = username
                        }
                    } else {
                        // Kullanıcı belirtilen dokümanı Firestore'da bulunmuyor
                    }
                }
                .addOnFailureListener { e ->
                    // Firestore verileri alınamadı
                }
        } else {
            // Kullanıcı oturum açmamış
        }

        binding.buttonAddToCart.setOnClickListener {
            totalFoodPrice = number * foodComing.yemek_fiyat

            val cartItems = viewModel.cartFoodList.value.orEmpty()

            val existingItem = cartItems.find { it.yemek_adi == foodComing.yemek_adi }

            if (existingItem == null) {
                addToCart(
                    yemek_adi = foodComing.yemek_adi,
                    yemek_resim_adi = url,
                    yemek_fiyat = totalFoodPrice,
                    yemek_siparis_adet = number,
                    kullanici_adi = userName
                )
                dismiss()
            } else {

                view?.let { view ->
                    Snackbar.make(view, "This product is already in the cart", Snackbar.LENGTH_SHORT).show()
                }
            }

        }


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val tempViewModel: FoodDetailViewModel by viewModels()
        viewModel = tempViewModel
    }

    fun addToCart(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) {
        viewModel.save(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        dismiss()
    }
}