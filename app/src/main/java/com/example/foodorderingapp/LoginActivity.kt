package com.example.foodorderingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.foodorderingapp.databinding.ActivityLoginBinding
import com.example.foodorderingapp.ui.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe changes in the current user data from the ViewModel
        viewModel.currentUser.observe(this) { user ->
            // If a user is logged in, navigate to the main activity and finish the login activity
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // Observe snackbar messages from the ViewModel and display them
        viewModel.snackbarMessage.observe(this, { message ->
            message?.let {
                showSnackbar(message)
            }
        })

        // Set up login button click listener
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmailLogin.text.toString()
            val password = binding.editTextPasswordLogin.text.toString()

            // Check if email and password are not empty, initiate the login process
            if (email.isEmpty() || password.isEmpty()) {
                showSnackbar("Please fill in both email and password fields.")
            } else {
                viewModel.login(email, password)
            }
        }

        // Set up click listener to navigate to the registration screen
        binding.textViewGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Helper function to display a Snackbar with the provided message
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}