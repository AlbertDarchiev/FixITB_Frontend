package com.example.fixitb_frontend.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val isLoading = mutableStateOf(false)

    var movistarCodeVal by mutableStateOf("") // changed only by changeFruit() method

    fun changeCode(value: String) {
        movistarCodeVal = value
    }
}