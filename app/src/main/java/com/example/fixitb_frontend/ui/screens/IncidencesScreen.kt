package com.example.fixitb_frontend.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.models.User
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

val usersList = listOf(
    User(1, "AAAAA", "albert@gmail.com"),User(1, "AAAAA", "albert@gmail.com"),User(1, "AAAAA", "albert@gmail.com"), User(2, "CCCCC", "assdsd@gmail.ocm"), User(3, "EEEEE", "FFFFF")
)
@Composable
fun IncidencesScreen(
    navController: NavHostController,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Blue1)
        )
    {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                alignment = Alignment.Center,
                painter = painterResource(id = R.drawable.img_logo_1),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Incidencies publicades",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp)
            )
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    items = usersList,
                    itemContent = {
                        userListItem(user = it)
                    })
            }
            Spacer(modifier = Modifier.height(10.dp))

            Spacer(modifier = Modifier.height(40.dp))

//        LoginButton(onSignInClick)

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "- o -",
                color = Color.White,
                style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .background(SecondaryColor.copy(alpha = 0.3f), shape = RoundedCornerShape(20.dp))
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally, ) {
                 Button(onClick = {
                     Firebase.auth.signOut()
                     navController.navigate("login")
//                     user = null
            }) {
                Text("Cerrar sesión")
            }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "No tens compte?",
                            color = Color.White,
                            style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        ClickableText(
                            text = AnnotatedString("Registra't"),
                            onClick = {
                                navController.navigate("register")
                            },
                        )

                    }

                }
            }

        }
    }
}

@Composable
fun userListItem(user: User) {
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .padding(10.dp)
//        .background(SecondaryColor.copy(alpha = 0.3f), shape = RoundedCornerShape(20.dp))
//    )
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .background(SecondaryColor.copy(alpha = 0.4f), shape = RoundedCornerShape(5.dp))
    ){
        Column {
            Text(text = user.email, style = typography.bodyLarge)
            Text(text = "VIEW DETAIL", style = typography.bodySmall)
            Text(text = "VIEW DETAIL", style = typography.bodySmall)
            Text(text = "VIEW DETAIL", style = typography.bodySmall)
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 640
)@Composable
fun IncidencesScreen() {
    val navController = rememberNavController()
    IncidencesScreen(navController = navController)
}