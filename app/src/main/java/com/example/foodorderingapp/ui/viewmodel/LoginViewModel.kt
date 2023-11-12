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

    // LiveData to hold the current user data
    private val _currentUser = MutableLiveData(authRepository.getCurrentUser())
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    // LiveData to hold the snackbar message for UI feedback
    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage

    /**
     * Attempt to sign in with the provided email and password.
     * If successful, update the current user LiveData; otherwise, display an error message.
     */
    fun login(email: String, password: String) {
        authRepository.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update the current user LiveData on successful login
                    _currentUser.value = authRepository.getCurrentUser()
                    Log.e("LoginViewModel", _currentUser.value.toString())
                } else {
                    // Display an error message in case of unsuccessful login
                    _snackbarMessage.value = "Login failed: ${task.exception?.message}"
                }
            }
    }
}
