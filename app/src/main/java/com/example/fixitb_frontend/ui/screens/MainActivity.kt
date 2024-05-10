package com.example.fixitb_frontend.ui.screens

import android.content.Context
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.fixitb_frontend.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fixitb_frontend.api.ApiViewModel.userService
import com.example.fixitb_frontend.viewmodel.MainViewModel
import com.example.fixitb_frontend.models.MyNavigationActions
import com.example.fixitb_frontend.models.MyNavigationDestination
import com.example.fixitb_frontend.models.MyNavigationRoute
import com.example.fixitb_frontend.models.Tokn
import com.example.fixitb_frontend.models.User
import com.example.fixitb_frontend.models.navigationDestinations
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object CurrentUser {
    var user: User? = null
    var userToken: String? = null
    var userFireb: FirebaseUser? = null
    var userGoogle : GoogleSignInAccount? = null
}

class MainActivity : ComponentActivity() {

    val mainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val navigateAction = remember(navController) {
                MyNavigationActions(navController)
            }
            mainViewModel.navController = navController


            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val selectedDestination = navBackStackEntry?.destination?.route ?: MyNavigationRoute.INCIDENCES

            MyAppContent(
                navController = navController,
                selectedDestination = selectedDestination,
                navigateTopLevelDestination = navigateAction::navigateTo,
                isLoadingState = mainViewModel.isLoading,
                viewModel = mainViewModel
            )
        }
    }
}
@Composable
fun ALogin(navController: NavHostController, user: FirebaseUser?, googleUser: GoogleSignInAccount? = null, context: Context?, token: String, launcher:
ManagedActivityResultLauncher<Intent, ActivityResult>? = null) {
    Column(
        modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "...")


        if (user == null ||googleUser == null || launcher == null){
            navController.navigate(MyNavigationRoute.LOGIN)
        }
        else{
            Log.d("GOOGLE token", googleUser.idToken.toString())
            LaunchedEffect(Unit) {
                try {
                    val response = userService.insertUser(Tokn(googleUser.idToken.toString()))
                    CurrentUser.userToken = response.body()?.token
                    CurrentUser.userFireb = user
                    CurrentUser.userGoogle = googleUser
                    CurrentUser.user = response.body()?.user

                    Log.d("CURRENT TOKEN >>>>>>", response.body()?.token.toString())
                    Log.d("CURRENT USER >>>>>>", response.body()?.user.toString())
                    Log.d("GOOGLE token", googleUser.idToken.toString())


                        while (CurrentUser.user == null) {
                            delay(100)
                        }

                    if (CurrentUser.user!!.role == "admin")
                        navController.navigate(MyNavigationRoute.INCIDENCES)
                    if (CurrentUser.user!!.role == "tecnic")
                        navController.navigate(MyNavigationRoute.INCIDENCES)
                    else
                        navController.navigate(MyNavigationRoute.INCIDENCES)

                }catch (e: Exception) {
                    navController.navigate(MyNavigationRoute.LOGIN)
                    Log.d("Sesió caducada", "Error signing in", e)
                }

            }
        }
    }
}

@Composable
fun rememberFirebaseAuthLauncher(
    navController: NavHostController,
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (Exception) -> Unit,
    context: Context? = null,
    isLoadingState: MutableState<Boolean>
): ManagedActivityResultLauncher<Intent, ActivityResult>{
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("Google Auth account", account.email.toString())
                val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
                scope.launch {
                    try {
                        Log.d("GOOGLE TOKEN", account.idToken.toString())
                        CurrentUser.userGoogle = account
                        val response = userService.insertUser(Tokn(account.idToken.toString()))
                        if (response.isSuccessful){
                            val authResult = Firebase.auth.signInWithCredential(credential).await()
                            onAuthComplete(authResult)
                            CurrentUser.user = response.body()?.user
                            CurrentUser.userFireb = authResult.user
                            CurrentUser.userToken = response.body()?.token
                            Log.d("CURRENT TOKEN >>>>>>", response.body()?.token.toString())
                            Log.d("CURRENT USER >>>>>>", response.body()?.user.toString())


                            navController.navigate(MyNavigationRoute.INCIDENCES)
                            delay(600)
                            isLoadingState.value = false
                        }
                        else{
                            Log.d("Google Auth", "Error signing in: ${response.code()}")
                            val responseBody = response.errorBody()!!.string()
                            Toast.makeText(context, responseBody, Toast.LENGTH_SHORT).show()
                            isLoadingState.value = false
                        }
                    }catch (e: Exception){
                        Log.d("Google Auth", "Error signing in: ", e)
                        Toast.makeText(context, "Problema de conexió", Toast.LENGTH_SHORT).show()
                        isLoadingState.value = false
                        }
                }
            }catch (e: Exception){
                Log.d("Google Auth", "Error signing in", e)
                onAuthError(e)
                isLoadingState.value = false
            } finally {
                isLoadingState.value = false
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
    isLoadingState : MutableState<Boolean>? = mutableStateOf(false),
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    var user by remember { mutableStateOf(Firebase.auth.currentUser)}
    var googleUser by remember { mutableStateOf(GoogleSignIn.getLastSignedInAccount(context))}
    val token = context.getString(R.string.google_client_id)
    val launcher = rememberFirebaseAuthLauncher(
        navController,
        onAuthComplete = { result ->
            user = result.user
        },
        onAuthError = { e ->
            user = null
            Log.d("Google Auth", "Error signing in", e)
        },
        context = context,
        isLoadingState = isLoadingState!!
    )
    val currentRoute by navController.currentBackStackEntryAsState()
    val showBottomNavigation = rememberSaveable { mutableStateOf(true) }

        if (CurrentUser.user != null && CurrentUser.user?.role == "admin") {
            showBottomNavigation.value = true
        } else {
            showBottomNavigation.value = false
    }

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
                    PostIncidenceScreen(navController, viewModel)
                }
                composable(MyNavigationRoute.USER_DETAILS) {
                    UsersDetailScreen(1)
                }
//                composable(MyNavigationRoute.USERS) {
//                    UsersScreen(navController, viewModel)
//                }
                composable(MyNavigationRoute.PROFILE) {
                    ProfileScreen(navController, viewModel)
                }
                composable(MyNavigationRoute.SPLASH) {
                    ALogin(navController, user, googleUser, context, token, launcher)
                }
                composable(MyNavigationRoute.LOGIN) {
                    LoginScreen(launcher, token, context, isLoadingState)
                }
                composable(MyNavigationRoute.CAMERA_BARCODE) {
                    CameraPreview(navController, viewModel)
                }
                composable(MyNavigationRoute.CAMERA) {
                    CameraScreen(navController, viewModel)
                }
            }

            // OCULTAR EL BOTTOM NAVIGATION EN SPLASH, LOGIN y CAMARA
            Log.d("ZZZZZZZZZZZ>>>>>>", CurrentUser.user.toString())
            Log.d("RRRRRRRRRRR>>>>>>", CurrentUser.user?.role.toString())

            if (showBottomNavigation.value && (
                    showBottomNavigation.value && currentRoute?.destination?.route !in listOf(
                    MyNavigationRoute.SPLASH,
                    MyNavigationRoute.LOGIN,
                    MyNavigationRoute.CAMERA,
                    MyNavigationRoute.CAMERA_BARCODE))) {
                MyBottomNavigation(
                    selectedDestination = selectedDestination,
                    navigateToDestination = navigateTopLevelDestination
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator() // Indicador de progreso circular
        Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical entre el indicador de progreso y el texto
        Text(text = "Cargando...") // Texto indicando que se está cargando
    }
}

//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    heightDp = 640
//)