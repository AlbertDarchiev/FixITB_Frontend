package com.example.fixitb_frontend.screens

import androidx.lifecycle.ViewModel
import com.example.fixitb_frontend.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessages
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}