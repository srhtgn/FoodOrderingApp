package com.example.foodorderingapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodorderingapp.LoginActivity
import com.example.foodorderingapp.databinding.FragmentMainPageBinding
import com.example.foodorderingapp.databinding.FragmentUserBinding
import com.example.foodorderingapp.ui.adapter.FoodsAdapter
import com.example.foodorderingapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderingapp.ui.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val userViewModel: UserViewModel by viewModels()

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get instances of Firebase Authentication and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    // Called when the fragment view is created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        // Get the current user from Firebase Authentication
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            // Fetch the username from the "users" collection in Firestore
            val uid = currentUser.uid
            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Set the username from Firestore to the textView
                        val username = documentSnapshot.getString("username")
                        binding.textViewCurrentUser.text = username
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

        // Set up the click listener for the logout button
        binding.buttonLogout.setOnClickListener {
            showLogoutConfirmationSnackbar()
        }

        return binding.root
    }

    // Show a Snackbar to confirm logout
    private fun showLogoutConfirmationSnackbar() {
        Snackbar.make(requireView(), "Are you sure you want to log out?", Snackbar.LENGTH_LONG)
            .setAction("Yes") {
                // If user confirms, sign out and navigate to the login activity
                firebaseAuth.signOut()
                navigateToLoginActivity()
            }
            .show()
    }

    // Navigate to the login activity
    private fun navigateToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
