package com.example.foodorderingapp.ui.fragment

import android.icu.math.BigDecimal
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodorderingapp.data.entity.CartFoods
import com.example.foodorderingapp.data.repository.AuthRepository
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

    var kullaniciAdi = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val currentUser = firebaseAuth.currentUser

        binding.rVCartFoods.layoutManager = LinearLayoutManager(requireContext())

        viewModel.cartFoodList.observe(viewLifecycleOwner) { cartFoods ->
            val cartAdapter = CartAdapter(requireContext(), cartFoods, viewModel)
            binding.rVCartFoods.adapter = cartAdapter

            totalPrice = BigDecimal.ZERO
            for (cartFood in cartFoods) {
                totalPrice = totalPrice.add(BigDecimal(cartFood.yemek_fiyat))
            }

            binding.textViewTotalPrice2.text = "Toplam Fiyat: ${totalPrice}₺"

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
                                kullaniciAdi = username
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
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val tempViewModel: CartViewModel by viewModels()
        viewModel = tempViewModel
    }



    override fun onResume() {
        super.onResume()
        viewModel.uploadCartFoods(kullanici_adi = kullaniciAdi)
    }
}
