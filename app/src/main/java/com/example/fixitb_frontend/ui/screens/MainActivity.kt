package com.example.fixitb_frontend.ui.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.fixitb_frontend.api.ApiViewModel
import com.example.fixitb_frontend.api.ApiViewModel.userService
import com.example.fixitb_frontend.models.User
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.example.fixitb_frontend.ui.theme.TertiaryColor
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import retrofit2.Response


// ...


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            var user by remember { mutableStateOf(Firebase.auth.currentUser) }
            val context = LocalContext.current
            val token = context.getString(R.string.google_client_id)
            val launcher = rememberFirebaseAuthLauncher(
                navController,
                onAuthComplete = { result ->
                    user = result.user
                },
                onAuthError = { e ->
                    user = null
                    Log.d("Google Auth", "Error signing in", e)
                }
            )

            NavHost(navController = navController, startDestination = "splash") {
                composable("login") {
                    LoginScreen(launcher, token, context)
                }
                composable("main") {
                    IncidencesScreen(navController)
                }
                composable("splash") {
                    ALogin(navController, user)
                }

                }
        }
    }
}
@Composable
fun ALogin(navController: NavHostController, user: FirebaseUser?) {
    Column {

        // HACER GET A API CON (user.email) PARA VER EL ROL DEL USUARIO CON ESE CORREO

        if (user == null){
            navController.navigate("login")
        }
        else{
            Log.d("USER", user.email.toString())
            val userInfo = User(1, "tecnic", "albert1979djy@gmail.com", 1)
//            val userInfo = User(2, "admin", "albert1979djy@gmail.com", 1)
//            val userInfo = User(3, "student", "albert1979djy@gmail.com", 1)
            if (userInfo.role == "admin")
                navController.navigate("main")
            else if (userInfo.role == "tecnic")
                navController.navigate("main")
            else
                navController.navigate("main")
//            Image(
//                painter = rememberAsyncImagePainter(user?.photoUrl),
//                contentDescription = null,
//                modifier = Modifier.size(100.dp))
//            Spacer(modifier = Modifier.height(20.dp))
//            Text(text = ("HOLA, ${user?.email}"))
//            Spacer(modifier = Modifier.height(20.dp))
//            Text(text = ("HOLA, ${user?.displayName}"))
//            Spacer(modifier = Modifier.height(20.dp))
//            Button(onClick = {
//                Firebase.auth.signOut()
//                user = null
//            }) {
//                Text("Cerrar sesión")
//            }
        }
    }
}

@Composable
fun rememberFirebaseAuthLauncher(
    navController: NavHostController,
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

                    authResult.user?.email
                    val userData = User(1, "student", authResult.user?.email.toString(), 1)

                    try {
                        val response = userService.insertUser(userData)
                        Log.d("LOGIN - RESPONSE", response.body().toString())
                        val responseUser : User = response.body()!!
                        Log.d("LOGIN - UserRole", response.body()!!.role)

                        if (responseUser.role == "admin")
                            navController.navigate("main")
                        else if (responseUser.role == "tecnic")
                            navController.navigate("main")
                        else
                            navController.navigate("main")
                    }
                    catch (e: Exception){
                        Log.d("LOGIN - ERROR", e.toString())
                    }
                }
            }catch (e: Exception){
                Log.d("Google Auth", "Error signing in", e)
                onAuthError(e)
            }
        }
    }
}


@Composable
fun LoginScreen1(navController: NavHostController) {
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
    }
}




//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    heightDp = 640
//)


