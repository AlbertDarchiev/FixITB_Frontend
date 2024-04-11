package com.example.fixitb_frontend.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp





@Composable
fun PaginaIncidencia() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // App bar
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Incidencia")
        }

        // Content
        Column(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            // Text fields
            Text("Quin dispositiu es ?")



            TextField(
                value = "Clase",
                onValueChange = { /* do something */ },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = "Codi dispositiu",
                onValueChange = { /* do something */ },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = "Codi movistar",
                onValueChange = { /* do something */ },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = "Quin problema hi ha ?",
                onValueChange = { /* do something */ },
                modifier = Modifier.fillMaxWidth()
            )

            // Button
            Button(
                onClick = { /* do something */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar Incidencia")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaginaIncidenciaPreview() {
    PaginaIncidencia()
}