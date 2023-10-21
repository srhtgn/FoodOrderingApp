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

        viewModel.currentUser.observe(this) { user ->
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // Snackbar mesajları için LiveData'yı dinle
        viewModel.snackbarMessage.observe(this, { message ->
            message?.let {
                showSnackbar(message)
            }
        })

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmailLogin.text.toString()
            val password = binding.editTextPasswordLogin.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                // ViewModel'daki login işlemini başlat,
                showSnackbar("Lütfen email ve şifre alanlarını doldurun.")

            } else {
                viewModel.login(email, password)            }
        }

        binding.textViewGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}