package com.example.foodorderingapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderingapp.data.repository.firebase.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    // LiveData to observe changes in the current user data
    val currentUser: LiveData<FirebaseUser?> = authRepository.getCurrentUserLiveData()

    // MutableLiveData to hold the user's username
    val username: MutableLiveData<String?> = MutableLiveData()

    /**
     * Coroutine function to fetch user details, including the username, based on the provided user ID.
     * If successful, update the username MutableLiveData; otherwise, handle the null username or any exceptions.
     */
    fun fetchUserDetails(uid: String) {
        viewModelScope.launch {
            try {
                val usernameResult = authRepository.fetchUserDetails(uid)
                if (usernameResult != null) {
                    // Update the username MutableLiveData on successful user details fetch
                    username.postValue(usernameResult)
                } else {
                    // Handle the case when the username is null after fetching user details
                    // Consider appropriate actions for this scenario.
                }
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the user details fetch
                // Consider appropriate error-handling actions for this scenario.
            }
        }
    }
}