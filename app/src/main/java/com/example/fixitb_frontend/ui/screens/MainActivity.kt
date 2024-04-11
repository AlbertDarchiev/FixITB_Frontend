package com.example.fixitb_frontend.ui.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixitb_frontend.R
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.fixitb_frontend.ui.screens.login.SignInScreen
import com.example.fixitb_frontend.ui.screens.login.SignInState
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.example.fixitb_frontend.ui.theme.TertiaryColor
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception


// ...


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    ALogin()
                }
//                composable("register") {
//                    SplashScreen(navController)
//                }
//                composable("main") {
//                    MainScreen(navController)
                }
        }
    }
}
@Composable
fun ALogin() {
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
        },
        onAuthError = { e ->
            user = null
            Log.d("Google Auth", "Error signing in", e)
        }
    )

    val context = LocalContext.current
    val token = context.getString(R.string.google_client_id)
    Log.d("TOKEN", token)

    Column {
        if (user == null){
            Button(onClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                launcher.launch(googleSignInClient.signInIntent)
                Log.d("LOGIN BUTTON", "CLICKED")
            }) {
                Text("Iniciar sesión")
            }
        }
        else{
            Image(
                painter = rememberAsyncImagePainter(user?.photoUrl),
                contentDescription = null,
                modifier = Modifier.size(100.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = ("HOLA, ${user?.email}"))
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = ("HOLA, ${user?.displayName}"))
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                Firebase.auth.signOut()

                user = null
            }) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (Exception) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult>{
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        scope.launch {
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("Google Auth account", account.toString())

                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                scope.launch {
                    val authResult = Firebase.auth.signInWithCredential(credential).await()
                    onAuthComplete(authResult)
                }
//                val authResult = Firebase.auth.signInWithCredential(credential).await()
//                onAuthComplete(authResult)
            }catch (e: Exception){
                Log.d("Google Auth", "Error signing in", e)
                onAuthError(e)
            }
        }
    }
}


@Composable
fun LoginScreen(navController: NavHostController) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Magenta)
        .blur(12.dp))
    {
        Image(
            painter = painterResource(id = R.drawable.server_img_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Escala la imagen para llenar el espacio
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
            text = "Iniciar sesió",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.White,
            style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp)
        )
        LoginButton(navController)




        SignInScreen(navController = navController, SignInState(), onSignInClick = {})




    }
}


@Composable
private fun LoginButton(navController: NavController){
    Button(onClick = {
        Log.d("TAG", "XXXXXXXXXXXX")
    },
        colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor),
        modifier = Modifier
            .width(250.dp)
            .height(60.dp)
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
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "Iniciar sesió",
                color = TertiaryColor,
                fontSize = 18.sp
            )
        }
    }
}
@Composable
fun SplashScreen(navController: NavHostController) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Magenta)
        .blur(12.dp))
    {
        Image(
            painter = painterResource(id = R.drawable.server_img_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Escala la imagen para llenar el espacio
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
            text = "Iniciar sesió",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.White,
            style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp)
        )
        LoginButton(navController)


        SignInScreen(navController = navController, SignInState(), onSignInClick = { print("AAAAAAAAAAAA") })




    }
}




//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    heightDp = 640
//)


