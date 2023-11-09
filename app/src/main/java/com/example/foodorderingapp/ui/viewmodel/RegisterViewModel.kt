package com.example.foodorderingapp.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderingapp.LoginActivity
import com.example.foodorderingapp.data.repository.firebase.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    var currentUser: LiveData<FirebaseUser?> = MutableLiveData()

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage

    private lateinit var context: Context // Context değişkeni

    fun setContext(context: Context) {
        this.context = context
    }

    init {
        currentUser = authRepository.getCurrentUserLiveData()
    }

    fun register(username: String, email: String, password: String) {
        authRepository.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = authRepository.getCurrentUser()
                    if (user != null) {
                        authRepository.saveUserData(user.uid, username, email)
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    }
                } else {
                    _snackbarMessage.value = "Kayıt başarısız: ${task.exception?.message}"
                }

            }
    }
}