//package com.example.fixitb_frontend.utils
//
//import android.app.Activity.RESULT_OK
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.IntentSenderRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.lifecycle.LifecycleCoroutineScope
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavController
//import com.example.fixitb_frontend.GoogleAuthClient
//import com.example.fixitb_frontend.screens.SignInScreen
//import com.example.fixitb_frontend.screens.SignInViewModel
//import kotlinx.coroutines.launch
//
//object LoginUtils {
//    @Composable
//    fun signIn(
//        navController: NavController,
//        googleAuthUiClient: GoogleAuthClient,
//        viewModel: SignInViewModel,
//        lifecycleScope: LifecycleCoroutineScope
//    ) {
//        val state by viewModel.state.collectAsStateWithLifecycle()
//        val user = googleAuthUiClient.getSignedInUser()
//        if (user != null) {
//            navController.navigate("main")
//        }
//
//        val launcher = rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.StartIntentSenderForResult(),
//            onResult = { result ->
//                if (result.resultCode == RESULT_OK) {
//                    lifecycleScope.launch {
//                        val signInResult = googleAuthUiClient.signInWithIntent(
//                            intent = result.data ?: return@launch
//                        )
//                        viewModel.onSignInResult(signInResult)
//                    }
//                }
//            }
//        )
//
//        LaunchedEffect(key1 = state.isSignInSuccessful) {
//            if (state.isSignInSuccessful) {
//                Toast.makeText(
//                    navController.context,
//                    "Sign in successful",
//                    Toast.LENGTH_LONG
//                ).show()
//
//                navController.navigate("login")
//                viewModel.resetState()
//            }
//        }
//
//        SignInScreen(
//            state = state,
//            onSignInClick = {
//                lifecycleScope.launch {
//                    val signInIntentSender = googleAuthUiClient.signIn()
//                    launcher.launch(
//                        IntentSenderRequest.Builder(
//                            signInIntentSender ?: return@launch
//                        ).build()
//                    )
//                }
//            }
//        )
//    }
//}
//
