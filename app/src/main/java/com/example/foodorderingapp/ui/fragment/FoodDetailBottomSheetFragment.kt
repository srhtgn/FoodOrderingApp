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
    var orderQuantity = 1
    var userName = ""

    // Called when the fragment view is created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodDetailBottomSheetBinding.inflate(inflater, container, false)

        // Get current user from FirebaseAuth
        val currentUser = firebaseAuth.currentUser

        // Get the food details from the arguments using Safe Args
        val bundle: FoodDetailBottomSheetFragmentArgs by navArgs()
        val foodComing = bundle.food
        val imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${foodComing.yemek_resim_adi}"

        // Load image into the ImageView using Glide library
        Glide.with(this)
            .load(imageUrl)
            .override(500)
            .into(binding.imageViewFood2)

        // Set food details in the UI
        binding.textViewFoodName2.text = foodComing.yemek_adi
        binding.textViewFoodPrice2.text = "${foodComing.yemek_fiyat}₺"
        binding.textViewTotalPrice.text = "Price: ${foodComing.yemek_fiyat}₺"

        // Increase the order quantity when the plus button is clicked
        binding.buttonPlus.setOnClickListener {
            orderQuantity++
            updateQuantityAndTotalPrice()
        }

        // Decrease the order quantity when the minus button is clicked
        binding.buttonMinus.setOnClickListener {
            if (orderQuantity > 1) {
                orderQuantity--
                updateQuantityAndTotalPrice()
            }
        }

        // Fetch current user details from Firestore
        currentUser?.let { user ->
            fetchUserDetails(user.uid)
        }

        // Add the selected food item to the cart
        binding.buttonAddToCart.setOnClickListener {
            totalFoodPrice = orderQuantity * foodComing.yemek_fiyat

            // Check if the item is already in the cart
            val cartItems = viewModel.cartFoodList.value.orEmpty()
            val existingItem = cartItems.find { it.yemek_adi == foodComing.yemek_adi }

            if (existingItem == null) {
                addToCart(foodComing.yemek_adi, imageUrl, totalFoodPrice, orderQuantity, userName)
                dismiss()
            } else {
                // Show a Snackbar message if the item is already in the cart
                view?.let { view ->
                    Snackbar.make(view, "This product is already in the cart", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FirebaseAuth and Firestore instances
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize FoodDetailViewModel using viewModels delegate
        val tempViewModel: FoodDetailViewModel by viewModels()
        viewModel = tempViewModel
    }

    // Function to update the quantity and total price in the UI
    private fun updateQuantityAndTotalPrice() {
        orderQuantity.let {
            binding.textViewNumber.text = it.toString()
            binding.textViewTotalPrice.text = "Price: ${it * totalFoodPrice}₺"
        }
    }

    // Function to fetch user details from Firestore
    private fun fetchUserDetails(uid: String) {
        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val username = documentSnapshot.getString("username")
                    username?.let {
                        userName = it
                    }
                } else {
                    // User document not found in Firestore
                }
            }
            .addOnFailureListener { e ->
                // Failed to retrieve Firestore data
            }
    }

    // Function to add the selected food item to the cart
    private fun addToCart(
        foodName: String,
        foodImageUrl: String,
        foodPrice: Int,
        orderQuantity: Int,
        username: String
    ) {
        viewModel.save(foodName, foodImageUrl, foodPrice, orderQuantity, username)
        dismiss()
    }
}