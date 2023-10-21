package com.example.foodorderingapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodorderingapp.LoginActivity
import com.example.foodorderingapp.databinding.FragmentMainPageBinding
import com.example.foodorderingapp.databinding.FragmentUserBinding
import com.example.foodorderingapp.ui.adapter.FoodsAdapter
import com.example.foodorderingapp.ui.viewmodel.MainPageViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase Authentication ve Firestore örneklerini alın
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        val currentUser = firebaseAuth.currentUser

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
                        binding.textViewCurrentUser.text = username
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

        // Çıkış yap butonuna tıklama işlemi
        binding.buttonLogout.setOnClickListener {
            showLogoutConfirmationSnackbar()
        }

        return binding.root
    }

    private fun showLogoutConfirmationSnackbar() {
        Snackbar.make(requireView(), "Çıkış yapmak istediğinize emin misiniz?", Snackbar.LENGTH_LONG)
            .setAction("Evet") {
                // Kullanıcı onaylarsa çıkış yap
                firebaseAuth.signOut()
                navigateToLoginActivity()
            }
            .show()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}