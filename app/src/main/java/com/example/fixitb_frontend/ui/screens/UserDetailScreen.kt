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


data class UserDetailData(
    val userId: Int,
    val name: String,
    val email: String,
    val publishedIncidents: Int,
    val activeIncidents: Int
)

@Composable
fun UsersDetailScreen(userId: Int) {
    // Creación de una instancia de UserData con nombre predefinido
    val userData = UserDetailData(
        userId = 5,
        name = "Roman Aziz",
        email = "muhammad-roman.aziz.7e5@itb.cat",
        publishedIncidents = usersList.count { it.userId == 5 }, // Contar incidencias publicadas
        activeIncidents = usersList.count { it.userId == 5 && (it.status == "open" || it.status == "revision") } // Contar incidencias activas
    )



    // Filtrar las incidencias para mostrar solo las del usuario especificado
    val userIncidences = usersList.filter { it.userId == userId }

    // Definir la estructura visual de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blue1.copy(alpha = 1.0f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Espacio alrededor para evitar bordes
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
                // Título de la pantalla
                Text(
                    modifier = Modifier
                        .padding(top = 20.dp),
                    text = "Detalls Usuari",
                    color = Color.White,
                    style = TextStyle(fontSize = 40.sp)
                )
                // Imagen de la empresa
                Image(
                    modifier = Modifier.size(75.dp),
                    painter = painterResource(id = R.drawable.img_logo_1),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Nombre del usuario
            Text(
                text = userData.name,
                color = Color.White,
                style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Box para mostrar otros datos del usuario
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SecondaryColor.copy(alpha = 0.4f), shape = RoundedCornerShape(5.dp))
                    .padding(8.dp)

            )
            {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Correo electrónico del usuario
                        Text("Correo: ${userData.email}", fontSize = 16.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))
                        // Número de incidencias publicadas por el usuario
                        Text(
                            "Incidencias Publicadas: ${userData.publishedIncidents}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        // Número de incidencias activas del usuario
                        Text(
                            "Incidencias Activas: ${userData.activeIncidents}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
            // Título del historial de incidencias
            Text(
                modifier = Modifier
                    .padding(top = 10.dp),
                text = "Historial",
                color = Color.White,
                style = TextStyle(fontSize = 40.sp)
            )

            // Recycler de incidencias del usuario
            LazyColumn(
                modifier = Modifier.fillMaxHeight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                // Mostrar las incidencias filtradas del usuario
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
                        )
                )
            }
        }
    }
}





@Preview
@Composable
fun UsersDetailScreenPreview() {
    // Pasar el userId del usuario en la lista de incidencias que deseas mostrar en la vista previa
    val userId = 5
    UsersDetailScreen(userId = userId)
}
