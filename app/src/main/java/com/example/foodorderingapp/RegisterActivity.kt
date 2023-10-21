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

        viewModel.setContext(this)

        viewModel.currentUser.observe(this) { user ->
            if (user != null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        viewModel.snackbarMessage.observe(this, { message ->
            message?.let {
                showSnackbar(message)
            }
        })

        binding.buttonRegister.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val email = binding.editTextEmailRegister.text.toString()
            val password = binding.editTextPasswordRegister.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showSnackbar("Lütfen tüm alanları doldurun.")
            } else {
                viewModel.register(username, email, password)
            }
        }

        binding.textViewGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}