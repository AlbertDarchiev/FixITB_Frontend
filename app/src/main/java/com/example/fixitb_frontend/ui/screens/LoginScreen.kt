package com.example.fixitb_frontend.ui.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun LoginScreen(launcher : ManagedActivityResultLauncher<Intent, ActivityResult>? = null, token : String? = null, context: Context? = null) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Magenta)
        .blur(12.dp)
    )
    {
        Image(
            painter = painterResource(id = R.drawable.server_img_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Spacer(modifier = Modifier.height(20.dp))
        Image(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            alignment = Alignment.Center,
            painter = painterResource(id = R.drawable.img_logo_1),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Iniciar sesiÃ³",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.White,
            style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp)
        )

        LoginButton(onSignInClick = {
            launcher
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token!!)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            launcher!!.launch(googleSignInClient.signInIntent)
        })
    }
}
@Composable
private fun LoginButton(onSignInClick: () -> Unit){
    Button(onClick = onSignInClick,
        colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor),
        modifier = Modifier
            .width(250.dp)
            .height(50.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.img_logo_google),
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .width(40.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Accedir amb Google",
                color = PrimaryColor,
                fontSize = 15.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(launcher = null, token = null, context = null)
}