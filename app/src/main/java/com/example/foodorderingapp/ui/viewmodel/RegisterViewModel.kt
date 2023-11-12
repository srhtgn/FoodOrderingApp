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

    // LiveData to hold the current user data
    var currentUser: LiveData<FirebaseUser?> = MutableLiveData()

    // LiveData to hold the snackbar message for UI feedback
    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage

    // Variable to hold the context
    private lateinit var context: Context

    // Function to set the context
    fun setContext(context: Context) {
        this.context = context
    }

    // Initialize the ViewModel by observing changes in the current user data
    init {
        currentUser = authRepository.getCurrentUserLiveData()
    }

    /**
     * Attempt to register a new user with the provided username, email, and password.
     * If successful, save user data, and navigate to the login screen; otherwise, display an error message.
     */
    fun register(username: String, email: String, password: String) {
        authRepository.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = authRepository.getCurrentUser()
                    if (user != null) {
                        // Save user data and navigate to the login screen on successful registration
                        authRepository.saveUserData(user.uid, username, email)
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    }
                } else {
                    // Display an error message in case of unsuccessful registration
                    _snackbarMessage.value = "Registration failed: ${task.exception?.message}"
                }
            }
    }
}
