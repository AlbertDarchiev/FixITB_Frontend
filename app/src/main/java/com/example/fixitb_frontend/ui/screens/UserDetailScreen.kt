package com.example.fixitb_frontend.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.api.ApiViewModel
import com.example.fixitb_frontend.models.Incidence
import com.example.fixitb_frontend.models.MyNavigationRoute
import com.example.fixitb_frontend.ui.composables.ComposableBoldText1
import com.example.fixitb_frontend.ui.composables.ComposableBoldText2
import com.example.fixitb_frontend.ui.composables.ComposableHeader
import com.example.fixitb_frontend.ui.composables.ComposableLightText1
import com.example.fixitb_frontend.ui.composables.ComposableNormalText1
import com.example.fixitb_frontend.ui.composables.ComposableNormalText2
import com.example.fixitb_frontend.ui.composables.buttons.GoBackButton
import com.example.fixitb_frontend.ui.theme.BackColor1
import com.example.fixitb_frontend.ui.theme.BackColor2
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.example.fixitb_frontend.ui.theme.TertiaryColor
import com.example.fixitb_frontend.viewmodel.MainViewModel
import com.example.fixitb_frontend.viewmodel.MainViewModel.navController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

suspend fun getIncidencesForUserAssigned(): List<Incidence> {
    val response = if (CurrentUser.user!!.role == "tecnic"){
        ApiViewModel.incidenceService.getIncidencesForUserAssigned(CurrentUser.user!!.email ,"Bearer " + CurrentUser.userToken)
    }
    else if (CurrentUser.user!!.role == "student"){
        ApiViewModel.incidenceService.getIncidencesForUser(CurrentUser.user!!.id ,"Bearer " + CurrentUser.userToken)
    } else {
        ApiViewModel.incidenceService.getIncidences("Bearer " + CurrentUser.userToken)
    }

    if (response.isSuccessful) {
        val incidences = response.body()
        if (incidences != null) {
            Log.d("EOO", "$incidences")
            return incidences
        } else {
            Log.e("INCIDENCES", "Response body is null")
            return emptyList()
        }
    } else {
        // Handle unsuccessful response
        Log.e("INCIDENCES", "Unsuccessful response: ${response.code()}")
        return emptyList()
    }
}

data class UserDetailData(
    val userId: Int,
    val email: String,
    val publishedIncidents: Int,
    val activeIncidents: Int
)

@Composable
fun UsersDetailScreen() {
    val usersListState = remember { mutableStateOf<List<Incidence>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    SideEffect {
        coroutineScope.launch {
            usersListState.value = getIncidencesForUserAssigned()
        }
    }


    val userDetails =
        if (CurrentUser.user!!.role == "tecnic") {
            UserDetailData(
                userId = CurrentUser.user!!.id!!.toInt(),
                email = CurrentUser.user!!.email,
                publishedIncidents = usersListState.value.count { it.userAssigned == CurrentUser.user!!.email },
                activeIncidents = usersListState.value.count { it.userAssigned == CurrentUser.user!!.email && (it.status == "obert" || it.status == "en proces") }
            )
        } else if (CurrentUser.user!!.role == "student") {
            UserDetailData(
                userId = CurrentUser.user!!.id!!.toInt(),
                email = CurrentUser.user!!.email,
                publishedIncidents = usersListState.value.count { it.userAssigned == CurrentUser.user!!.email },
                activeIncidents = usersListState.value.count { it.userAssigned == CurrentUser.user!!.email && (it.status == "obert" || it.status == "en proces") }
            )
        } else {
            UserDetailData(
                userId = CurrentUser.user!!.id!!.toInt(),
                email = CurrentUser.user!!.email,
                publishedIncidents = usersListState.value.count { it.id != 0 },
                activeIncidents = usersListState.value.count { it.status == "obert" || it.status == "en proces" }
            )
        }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        BackColor1,
                        BackColor2
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GoBackButton(navController = navController!!, destination = MyNavigationRoute.INCIDENCES)
                ComposableHeader(text = "DETALLS USUARI", size = 28, width = 1f)
            }

            //// DETALLES DEL USUARIO ////
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(PrimaryColor, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            )
            {
                    Column(modifier = Modifier) {
                        Row {
                            ComposableBoldText2("Correu: ", 16)
                            ComposableNormalText2(userDetails.email, 15)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        if (CurrentUser.user!!.role == "tecnic"){
                            Row {
                                ComposableBoldText2("Incidències assignades: ", 16)
                                ComposableNormalText2(userDetails.publishedIncidents.toString(), 15)
                            }
                        }
                        else if (CurrentUser.user!!.role == "student"){
                            Row {
                                ComposableBoldText2("Incidències publicades: ", 16)
                                ComposableNormalText2(userDetails.publishedIncidents.toString(), 15)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row {
                            ComposableBoldText2("Incidències actives: ", 16)
                            ComposableNormalText2(userDetails.activeIncidents.toString(), 15)
                        }
                        val context = LocalContext.current
                        val token = context.getString(R.string.google_client_id)
                        Box(modifier =  Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Button(
                                onClick = {
                                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(token)
                                        .requestEmail()
                                        .build()

                                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                                    googleSignInClient.signOut()
                                    FirebaseAuth.getInstance().signOut()
                                    navController!!.navigate(MyNavigationRoute.LOGIN)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor, contentColor = TertiaryColor.copy(alpha = 0.8f)),
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            ) {
                                Text("Tancar sessió")
                            }
                        }

                    }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)) {
                //// MOSTRAR TITULO (segun el rol del usuario) ////
                if (CurrentUser.user!!.role == "tecnic")
                    ComposableLightText1(text = "TASQUES ASSIGNADES", 30)
                else if (CurrentUser.user!!.role == "student")
                    ComposableLightText1(text = "HISTORIAL", 30)
                else
                    ComposableLightText1(text = "INCIDÈNCIES", 30)


                LazyColumn(
                    modifier = Modifier.fillMaxHeight(1f),
                ) {
                    items(
                        items = usersListState.value,
                        itemContent = {
                            incidenceListItem(incidence = it)
                        }
                    )
                }
            }

        }
    }
}


@Preview
@Composable
fun UsersDetailScreenPreview() {
    val userId = CurrentUser.user!!.id!!.toInt()
    UsersDetailScreen()
}
