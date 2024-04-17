package com.example.fixitb_frontend.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixitb_frontend.R

@Composable
fun PostIncidenceScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Espacio alrededor para evitar bordes
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            DropdownMenu(
                expanded = false, // Cambiar a true para ver el menú desplegable
                onDismissRequest = { /* Lógica para cerrar el menú */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Agregar elementos del menú desplegable aquí
            }
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
                    text = "Incidencia",
                    color = Color.White,
                    style = TextStyle(fontSize = 45.sp)
                )
                Image(
                    modifier = Modifier.size(75.dp),
                    painter = painterResource(id = R.drawable.img_logo_1),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = "", // Ajusta el valor según sea necesario
                onValueChange = { /* Notificar cambios */ },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ingrese su incidencia") }
            )
        }
    }
}

@Preview
@Composable
fun PreviewPostIncidenceScreen() {
    PostIncidenceScreen()
}
