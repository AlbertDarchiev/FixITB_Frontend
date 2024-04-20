package com.example.fixitb_frontend.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoadingViewModel : ViewModel() {
    val isLoading = mutableStateOf(false)
}