package com.example.foodorderingapp.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

fun Navigation.transition(view: View, id: Int){
    findNavController(view).navigate(id)
}

fun Navigation.transition(view: View, id: NavDirections){
    findNavController(view).navigate(id)
}

fun Context.displayToast(massage: String) {
    Toast.makeText(this, massage, Toast.LENGTH_SHORT).show()
}