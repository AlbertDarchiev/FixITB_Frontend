package com.example.fixitb_frontend.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.input.TextFieldValue
import com.example.fixitb_frontend.ui.theme.PrimaryColor


@Composable
fun FixIncidenceScreen() {

    val incidence = usersList[2] // Cambia el índice según sea necesario

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
                    text = "Detalls Incidencia",
                    color = Color.White,
                    style = TextStyle(fontSize = 35.sp)
                )
                // Imagen de la empresa
                Image(
                    modifier = Modifier.size(75.dp),
                    painter = painterResource(id = R.drawable.img_logo_1),
                    contentDescription = null
                )
            }
            //numero incidencia

            Column {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Incidencia: # ${incidence.id}",
                    color = Color.White,
                    style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = incidence.title,
                    color = Color.White,
                    style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            //box con los detalles

            // muestra las fechas

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            SecondaryColor.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(2.dp)
                ) {
                    Text(
                        text = "Open Date: ${incidence.openDate}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp)) // Espacio entre las cajas
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            SecondaryColor.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(2.dp)
                ) {
                    Text(
                        text = "Close Date: ${incidence.closeDate}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Muestra todos los datos

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SecondaryColor.copy(alpha = 0.4f), shape = RoundedCornerShape(5.dp))
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Obtener la incidencia deseada

                    // Mostrar los campos de la incidencia
                    Text(text = "Aula: ${incidence.classNum}", color = Color.White)
                    Text(text = "Dispositiu: ${incidence.device}", color = Color.White)
                    Text(text = "Codi: ${incidence.codeMain}", color = Color.White)
                    Text(text = "Codi Movistar: ${incidence.codeMovistar}", color = Color.White)
                    Text(text = "Descripció: ${incidence.description}", color = Color.White)

                    Text(text = "Image: ${incidence.image}", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
//Part del comentari tecnic

            var text by remember { mutableStateOf(TextFieldValue()) }
            val scrollState = rememberScrollState()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .background(
                           Color.White.copy(alpha = 1.0f),
                           shape = RoundedCornerShape(5.dp))
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Comentario Técnico",
                        color = Color.Black,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(8.dp)
                    )
                    BasicTextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                onClick = {
                    // Logica de la funcionalidad
                }
            ) {
                Text("Publicar Incidencia")
            }



        }
    }
}





@Preview
@Composable
fun FixIncidenceScreenPreview() {
    FixIncidenceScreen()
}