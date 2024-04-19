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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fixitb_frontend.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fixitb_frontend.api.ApiViewModel.userService
import com.example.fixitb_frontend.models.MyNavigationActions
import com.example.fixitb_frontend.models.MyNavigationDestination
import com.example.fixitb_frontend.models.MyNavigationRoute
import com.example.fixitb_frontend.models.User
import com.example.fixitb_frontend.models.navigationDestinations
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

object CurrentUser {
    var userFireb: FirebaseUser? = null
    var user : User? = null
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val navigateAction = remember(navController) {
                MyNavigationActions(navController)
            }
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val selectedDestination = navBackStackEntry?.destination?.route ?: MyNavigationRoute.INCIDENCES
            MyAppContent(
                navController = navController,
                selectedDestination = selectedDestination,
                navigateTopLevelDestination = navigateAction::navigateTo
            )
        }
    }
}

suspend fun getUser(user: FirebaseUser): User {
    val response = userService.getUserByEmail(user?.email.toString())
    return response.body()!!
}

@Composable
fun ALogin(navController: NavHostController, user: FirebaseUser?) {
    Column {
        // HACER GET A API CON (user.email) PARA VER EL ROL DEL USUARIO CON ESE CORREO
        if (user == null){
            navController.navigate(MyNavigationRoute.LOGIN)
        }
        else{
            CurrentUser.userFireb = user
            Log.d("USER", user.email.toString())
            val userInfo = User(1, "tecnic", "albert1979djy@gmail.com")
//            val userInfo = User(2, "admin", "albert1979djy@gmail.com", 1)
//            val userInfo = User(3, "student", "albert1979djy@gmail.com", 1)
            if (userInfo.role == "admin")
                navController.navigate(MyNavigationRoute.INCIDENCES)
            else if (userInfo.role == "tecnic")
                navController.navigate(MyNavigationRoute.INCIDENCES)
            else
                navController.navigate(MyNavigationRoute.INCIDENCES)
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
//                Text("Cerrar sesi贸n")
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
                    val userData = User(1, "student", authResult.user?.email.toString())

                    try {
                        val response = userService.insertUser(userData)
                        Log.d("LOGIN - RESPONSE", response.body().toString())
                        val responseUser : User = response.body()!!
                        Log.d("LOGIN - UserRole", response.body()!!.role)

                        if (responseUser.role == "admin")
                            navController.navigate(MyNavigationRoute.INCIDENCES)

                        else if (responseUser.role == "tecnic")
                            navController.navigate(MyNavigationRoute.INCIDENCES)

                        else
                            navController.navigate(MyNavigationRoute.INCIDENCES)

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
fun MyBottomNavigation(
    selectedDestination: String,
    navigateToDestination: (MyNavigationDestination) -> Unit
){
    NavigationBar(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.08f),
        containerColor = SecondaryColor,
    ) {
        navigationDestinations.forEach { destination ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryColor,
                    unselectedIconColor = PrimaryColor
                ),
                selected = destination.route == selectedDestination,
                onClick = {
                    navigateToDestination(destination)
                },
                icon = {
                    Image(
                        imageVector = destination.selectedIcon,
                        contentDescription = destination.iconText,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun MyAppContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    selectedDestination: String,
    navigateTopLevelDestination: (MyNavigationDestination) -> Unit,
) {
    var user by remember { mutableStateOf(Firebase.auth.currentUser)}
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

    val currentRoute by navController.currentBackStackEntryAsState()
    val showBottomNavigation = rememberSaveable { mutableStateOf(true) }

    Row(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = MyNavigationRoute.SPLASH
            ) {
                composable(MyNavigationRoute.INCIDENCES) {
                    IncidencesScreen(navController)
                }
                composable(MyNavigationRoute.INCIDENCE_POST) {
                    PostIncidenceScreen()
                }
                composable(MyNavigationRoute.USERS) {
                    UsersScreen()
                }
                composable(MyNavigationRoute.SPLASH) {
                    ALogin(navController = navController, user = user)
                }
                composable(MyNavigationRoute.LOGIN) {
                    LoginScreen(launcher, token, context)
                }

            }

            // OCULTAR EL BOTTOM NAVIGATION EN SPLASH Y LOGIN
            if (showBottomNavigation.value && currentRoute?.destination?.route !in listOf(
                    MyNavigationRoute.SPLASH,
                    MyNavigationRoute.LOGIN)) {
                MyBottomNavigation(
                    selectedDestination = selectedDestination,
                    navigateToDestination = navigateTopLevelDestination
                )
            }
        }
    }
}
//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    heightDp = 640
//)