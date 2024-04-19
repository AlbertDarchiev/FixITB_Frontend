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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
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
import com.example.fixitb_frontend.models.MyNavigationRoute
import com.example.fixitb_frontend.ui.composables.ComposableBoldText2
import com.example.fixitb_frontend.ui.composables.ComposableNormalText2
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.example.fixitb_frontend.ui.theme.rowdiesFontFamily
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

val usersList = listOf(
    Incidence(1, "Monitor", "albert@gmail.com", "Boton del monitor no funciona","description", "2024-04-22", "2024-04-22", "open", 306, "albert.darchiev.7e6@itb.cat", "123123", 123123),
    Incidence(1, "Altre", "albert@gmail.com", "Falta monitor", "description", "2024-04-22", "2024-04-22", "open", 209, "albert.darchiev.7e6@itb.cat", "123123", 123123),
    Incidence(1, "Monitor", "albert@gmail.com", "El monitor esta trencat","description", "2024-04-22", "2024-04-22", "closed", 309, "albert.darchiev.7e6@itb.cat", "123123", 123123),
    Incidence(1, "Altre", "albert@gmail.com", "Falta cable ethernet","description", "2024-04-22", null, "revision", 309, "albert.darchiev.7e6@itb.cat", "123123", 123123),
    Incidence(1, "Teclado", "albert@gmail.com", "No hi ha teclat","description", "2024-04-22", "2024-04-22", "closed", 104, "albert.darchiev.7e6@itb.cat", "123123", 123123),
    Incidence(1, "Ordinador", "albert@gmail.com", "Ordinador no arranca","description", "2024-04-22", "2024-04-22", "open", 201, "albert.darchiev.7e6@itb.cat", "123123", 123123),
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
        Button(onClick = {
            Firebase.auth.signOut()
            navController.navigate(MyNavigationRoute.LOGIN)
        }) {
            Column {
                Image(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "EXIT",
                    modifier = Modifier.size(24.dp)
                )
                Text("Cerrar sesión", fontSize = 10.sp)
            }

        }

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
                text = "INCIDÈNCIES",
                fontFamily = rowdiesFontFamily,
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
            )
            LazyColumn(
                modifier = Modifier.fillMaxHeight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    items = usersList,
                    itemContent = {
                        incidenceListItem(incidence = it)
                    })
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
            ComposableBoldText2(text = incidence.title, fontSize = 16)
            ComposableNormalText2(text = "Aula ${incidence.classNum} · ${incidence.device}", fontSize = 14)
            Row {
                ComposableNormalText2(text = incidence.openDate+" - ", fontSize = 14)
                if (incidence.closeDate != null)
                    ComposableNormalText2(text = incidence.closeDate, fontSize = 14)
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
fun IncidencesScreenPrev() {
    val navController = rememberNavController()
    IncidencesScreen(navController = navController)
}