package com.example.fixitb_frontend.viewmodel

import android.Manifest
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.fixitb_frontend.api.ApiViewModel
import com.example.fixitb_frontend.models.Incidence
import retrofit2.Response

object MainViewModel : ViewModel() {
    var navController by mutableStateOf<NavController?>(null)
    val isLoading = mutableStateOf(false)
    var movistarCodeVal by mutableStateOf("")
    var currentImageUri by mutableStateOf<Uri>(Uri.EMPTY)

    fun changeCode(value: String) {
        movistarCodeVal = value
    }


    var launchCameraEvent = mutableStateOf(false)

    fun launchCamera() {
        launchCameraEvent = mutableStateOf(true)
    }



    fun requestCameraPermission(permissionLauncher: ActivityResultLauncher<String>) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }


    suspend fun updateUserRole(userId: Int, newRole: String, token: String): Response<Unit> {
        return ApiViewModel.userService.updateUserRole(userId, newRole, "Bearer $token")
    }
}