package com.example.fixitb_frontend.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.models.Incidence
import com.example.fixitb_frontend.ui.composables.ComposableBoldText1
import com.example.fixitb_frontend.ui.composables.ComposableBoldText2
import com.example.fixitb_frontend.ui.composables.ComposableNormalText1
import com.example.fixitb_frontend.ui.composables.ComposableNormalText2
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.google.android.gms.auth.api.signin.GoogleSignIn

data class UserDetailData(
    val userId: Int,
    val name: String,
    val email: String,
    val publishedIncidents: Int,
    val activeIncidents: Int
)

@Composable
fun UsersDetailScreen(userId: Int) {

    val exampleIncidenceList = listOf(
        Incidence(
            id = 1,
            device = "Device 1",
            image = "Image URL 1",
            title = "Title 1",
            description = "Description 1",
            openDate = "2024-05-06",
            closeDate = "2024-05-10",
            status = "open",
            classNum = 101,
            userAssigned = "Technician 1",
            codeMain = "Code Main 1",
            codeMovistar = 123,
            userId = 1
        ),
        Incidence(
            id = 2, // Cambiar el ID para la segunda incidencia
            device = "Device 2", // Cambiar los detalles según sea necesario
            image = "Image URL 2",
            title = "Title 2",
            description = "Description 2",
            openDate = "2024-05-07", // Cambiar las fechas según sea necesario
            closeDate = "2024-05-11",
            status = "closed",
            classNum = 102,
            userAssigned = "Technician 2",
            codeMain = "Code Main 2",
            codeMovistar = 456,
            userId = 1 // Asignar el mismo ID de usuario que el ID de usuario que pasas a la función
        ),
    )

    val userData = UserDetailData(
        userId = 1,
        name = "Roman Aziz",
        email = "muhammad-roman.aziz.7e5@itb.cat",
        publishedIncidents = exampleIncidenceList.count { it.userId == 1 },
        activeIncidents = exampleIncidenceList.count { it.userId == 1 && (it.status == "open" || it.status == "revision") }
    )

    val userIncidences = exampleIncidenceList.filter { it.userId == userId }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blue1.copy(alpha = 1.0f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 20.dp),
                    text = "Detalls Usuari",
                    color = Color.White,
                    style = TextStyle(fontSize = 40.sp)
                )
                Image(
                    modifier = Modifier.size(75.dp),
                    painter = painterResource(id = R.drawable.img_logo_1),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = CurrentUser.user!!.email,
                color = Color.White,
                style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SecondaryColor.copy(alpha = 0.4f), shape = RoundedCornerShape(5.dp))
                    .padding(8.dp)
            )
            {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Correo: ${userData.email}", fontSize = 16.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Incidencias Publicadas: ${userData.publishedIncidents}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Incidencias Activas: ${userData.activeIncidents}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
            Text(
                modifier = Modifier
                    .padding(top = 10.dp),
                text = "Historial",
                color = Color.White,
                style = TextStyle(fontSize = 40.sp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxHeight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = userIncidences,
                    itemContent = {
                        IncidenceUserListItem(incidence = it)
                    }
                )
            }
        }
    }
}

@Composable
fun IncidenceUserListItem(incidence: Incidence) {
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
                ComposableNormalText2(text = incidence.closeDate.toString(), fontSize = 14)
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
                        )
                )
            }
        }
    }
}

@Preview
@Composable
fun UsersDetailScreenPreview() {
    val userId = 1
    UsersDetailScreen(userId = userId)
}
