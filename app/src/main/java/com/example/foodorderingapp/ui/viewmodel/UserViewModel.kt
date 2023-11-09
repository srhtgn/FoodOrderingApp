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
    val currentUser: LiveData<FirebaseUser?> = authRepository.getCurrentUserLiveData()
    val username: MutableLiveData<String?> = MutableLiveData()

    fun fetchUserDetails(uid: String) {
        viewModelScope.launch {
            try {
                val usernameResult = authRepository.fetchUserDetails(uid)
                if (usernameResult != null) {
                    username.postValue(usernameResult)
                } else {
                    // Kullanıcı adı null ise nasıl işlem yapılacağını belirleyin.
                }
            } catch (e: Exception) {
                // Hata durumunu nasıl ele alacağınızı belirleyin.
            }
        }
    }


}