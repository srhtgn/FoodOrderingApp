package com.example.foodorderingapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderingapp.data.repository.firebase.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _currentUser = MutableLiveData(authRepository.getCurrentUser())
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage

    fun login(email: String, password: String) {
        authRepository.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _currentUser.value = authRepository.getCurrentUser()
                    Log.e("LoginViewModel", _currentUser.value.toString())
                } else {
                    _snackbarMessage.value = "Giriş başarısız: ${task.exception?.message}"
                }
            }
    }
}