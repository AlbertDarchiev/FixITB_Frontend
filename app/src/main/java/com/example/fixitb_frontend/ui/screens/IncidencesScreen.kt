package com.example.fixitb_frontend.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.models.Incidence
import com.example.fixitb_frontend.ui.composables.ComposableBoldText
import com.example.fixitb_frontend.ui.composables.ComposableNormalText
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

val usersList = listOf(
    Incidence(1, "Portatil", "albert@gmail.com", "description", "2024-04-22", "2024-04-22", "open", 1, "albert.darchiev.7e6@itb.cat", "123123", 123123),
    Incidence(1, "Portatil", "albert@gmail.com", "description", "2024-04-22", "2024-04-22", "open", 1, "albert.darchiev.7e6@itb.cat", "123123", 123123),
    Incidence(1, "Teclado", "albert@gmail.com", "description", "2024-04-22", "2024-04-22", "closed", 1, "albert.darchiev.7e6@itb.cat", "123123", 123123),
    Incidence(1, "Ordenador", "albert@gmail.com", "description", "2024-04-22", null, "revision", 1, "albert.darchiev.7e6@itb.cat", "123123", 123123),
    Incidence(1, "Teclado", "albert@gmail.com", "description", "2024-04-22", "2024-04-22", "closed", 1, "albert.darchiev.7e6@itb.cat", "123123", 123123),
    Incidence(1, "Monitor", "albert@gmail.com", "description", "2024-04-22", "2024-04-22", "open", 1, "albert.darchiev.7e6@itb.cat", "123123", 123123),
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
                modifier = Modifier.fillMaxHeight(0.8f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    items = usersList,
                    itemContent = {
                        incidenceListItem(incidence = it)
                    })
            }
            Spacer(modifier = Modifier.height(10.dp))

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
                    }

                }
            }
        }
    }

@Composable
fun incidenceListItem(incidence: Incidence) {
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
        Column(modifier = Modifier.padding(8.dp)) {
            ComposableBoldText(text = "Boton monitor no funciona ", fontSize = 16)
            ComposableNormalText(text = "Dispositiu: "+incidence.device, fontSize = 14)
            Row {
                ComposableNormalText(text = incidence.openDate+" - ", fontSize = 14)
                if (incidence.closeDate != null)
                    ComposableNormalText(text = incidence.closeDate, fontSize = 14)
                Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                shape = CircleShape,
                                color =
                                if (incidence.status == "open")
                                    Color.Green
                                else if (incidence.status == "closed")
                                    Color.Red else
                                    Color.Yellow
                            ))
            }
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