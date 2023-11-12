package com.example.foodorderingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.foodorderingapp.databinding.ActivityMainBinding
import com.example.foodorderingapp.databinding.ActivityRegisterBinding
import com.example.foodorderingapp.ui.viewmodel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the context for the ViewModel
        viewModel.setContext(this)

        // Observe changes in the current user data from the ViewModel
        viewModel.currentUser.observe(this) { user ->
            // If a user is logged in, navigate to the login activity and finish the register activity
            if (user != null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        // Observe snackbar messages from the ViewModel and display them
        viewModel.snackbarMessage.observe(this, { message ->
            message?.let {
                showSnackbar(message)
            }
        })

        // Set up register button click listener
        binding.buttonRegister.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val email = binding.editTextEmailRegister.text.toString()
            val password = binding.editTextPasswordRegister.text.toString()

            // Check if username, email, and password are not empty, initiate the registration process
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showSnackbar("Please fill in all fields.")
            } else {
                viewModel.register(username, email, password)
            }
        }

        // Set up click listener to navigate to the login screen
        binding.textViewGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Helper function to display a Snackbar with the provided message
    fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}