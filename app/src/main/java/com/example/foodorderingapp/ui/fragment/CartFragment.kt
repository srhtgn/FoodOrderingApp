package com.example.foodorderingapp.ui.fragment

import android.icu.math.BigDecimal
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.data.repository.firebase.AuthRepository
import com.example.foodorderingapp.databinding.FragmentCartBinding
import com.example.foodorderingapp.ui.adapter.CartAdapter
import com.example.foodorderingapp.ui.viewmodel.CartViewModel
import com.example.foodorderingapp.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private var totalPrice: BigDecimal = BigDecimal.ZERO
    private lateinit var authRepository: AuthRepository
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var currentUsername = ""

    // Called when the fragment view is created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        // Set up the RecyclerView for cart foods
        setupCartRecyclerView()

        return binding.root
    }

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeFirebaseInstances()

        // Initialize CartViewModel using viewModels delegate
        val tempViewModel: CartViewModel by viewModels()
        viewModel = tempViewModel
    }

    // Called when the fragment is resumed
    override fun onResume() {
        super.onResume()

        // Fetch the current user details
        fetchCurrentUser()
    }

    // Set up the RecyclerView for displaying cart foods
    private fun setupCartRecyclerView() {
        binding.rVCartFoods.layoutManager = LinearLayoutManager(requireContext())
        viewModel.cartFoodList.observe(viewLifecycleOwner) { cartFoods ->
            val cartAdapter = CartAdapter(requireContext(), cartFoods, viewModel)
            binding.rVCartFoods.adapter = cartAdapter

            // Calculate the total price of cart foods
            totalPrice = BigDecimal.ZERO
            for (cartFood in cartFoods) {
                totalPrice = totalPrice.add(BigDecimal(cartFood.yemek_fiyat))
            }

            // Display the total price
            binding.textViewTotalPrice2.text = "Total Price: ${totalPrice}₺"
            binding.progressBarCart.visibility = View.GONE
        }
    }

    // Initialize Firebase instances (Authentication and Firestore)
    private fun initializeFirebaseInstances() {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    // Fetch the details of the current user from Firestore
    private fun fetchCurrentUser() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val username = documentSnapshot.getString("username")
                        if (username != null) {
                            currentUsername = username

                            // Upload cart foods for the current user
                            viewModel.uploadCartFoods(kullanici_adi = currentUsername)
                        }
                    } else {
                        // User document not found in Firestore
                    }
                }
                .addOnFailureListener { e ->
                    // Failed to retrieve Firestore data
                }
        } else {
            // User is not signed in
        }
    }
}
