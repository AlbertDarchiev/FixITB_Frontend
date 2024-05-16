package com.example.fixitb_frontend.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.fixitb_frontend.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: ViewModel) {
    val context = LocalContext.current
    val token = context.getString(R.string.google_client_id)
    Column {
        //// SERRAR SESION EN GOOGLE ////
        Button(onClick = {

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            googleSignInClient.signOut()
            FirebaseAuth.getInstance().signOut()
            navController.navigate("login")
        }
        ){
            Text(text = "Tancar sessió")
        }
    }

}
