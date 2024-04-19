package com.example.fixitb_frontend.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.TertiaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostIncidenceScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blue1.copy(alpha = 1.0f))    ) {
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


            //Comineza el dropdown menu para que dispositivo es

            val listDispositiu = listOf("Torre","Monitor","Teclat","Ratoli")

            var selectedText by remember { mutableStateOf(listDispositiu[0]) }

            var isExpanded by remember {
                mutableStateOf(false)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                ExposedDropdownMenuBox(
                    expanded = isExpanded ,
                    onExpandedChange = {isExpanded = !isExpanded}
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(),
                        value = selectedText ,
                        onValueChange = {},
                        label = { Text("Quin dispositiu es ?") } ,
                        readOnly = true,
                        trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)}
                    )
                    
                    ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = {isExpanded = false}) {
                        listDispositiu.forEachIndexed{ index, text ->
                            DropdownMenuItem(
                                text = { Text(text = text) },
                                onClick = {
                                    selectedText = listDispositiu[index]
                                    isExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
                Text(
                    text = "Currently selected: $selectedText",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            //Acaba el dropdown menu para que dispositivo es

//Comineza el dropdown menu para que dispositivo es

            val listAula = listOf("Aula 001","Aula 002","Aula 003","Aula 004","Aula 005","Aula 006",
                "Aula 007","Aula 008","Aula 009","Aula 010","Aula 011","Aula 012","Aula 013","Aula 014",
                "Aula 015","Aula 016","Aula 017","Aula 018","Aula 019","Aula 020","Aula 021","Aula 022",
                "Aula 023","Aula 024","Aula 025","Aula 026","Aula 027","Aula 099",
                "Aula 101","Aula 102","Aula 103","Aula 104","Aula 105","Aula 106","Aula 107","Aula 108",
                "Aula 109","Aula 110","Aula 111","Aula 112",
                "Aula 201","Aula 202","Aula 203","Aula 204","Aula 205","Aula 206","Aula 207","Aula 208",
                "Aula 209","Aula 210","Aula 211","Aula 212",
                "Aula 301","Aula 302","Aula 303","Aula 304","Aula 305","Aula 306","Aula 307","Aula 308",
                "Aula 309","Aula 310","Aula 311","Aula 312")

            var selectedTextAula by remember { mutableStateOf(listAula[0]) }

            var isExpandedAula by remember {
                mutableStateOf(false)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                ExposedDropdownMenuBox(
                    expanded = isExpandedAula ,
                    onExpandedChange = {isExpandedAula = !isExpandedAula}
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(),
                        value = selectedTextAula ,
                        onValueChange = {},
                        label = { Text("Quina aula es ?") } ,
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedAula)}
                    )

                    ExposedDropdownMenu(expanded = isExpandedAula, onDismissRequest = {isExpandedAula = false}) {
                        listAula.forEachIndexed{ index, text ->
                            DropdownMenuItem(
                                text = { Text(text = text) },
                                onClick = {
                                    selectedTextAula = listAula[index]
                                    isExpandedAula = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
                Text(
                    text = "Currently selected: $selectedTextAula",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
//Acaba el dropdown menu que aula es
//Empiezan text fields
            TextField(
                value = "", // Ajusta el valor según sea necesario
                onValueChange = { /* Notificar cambios */ },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Codi Dispositiu") }
            )
            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = "", // Ajusta el valor según sea necesario
                onValueChange = { /* Notificar cambios */ },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Codi Movistar Dispositiu") }
            )
            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = "", // Ajusta el valor según sea necesario
                onValueChange = { /* Notificar cambios */ },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ingrese su incidencia") }
            )
            Spacer(modifier = Modifier.height(20.dp))

//Acaba textfields
// insertar imagen


            val imageUri by remember { mutableStateOf<Uri?>(null) }

            val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    // La foto fue tomada exitosamente, puedes hacer algo con la Uri de la imagen
                }
            }
            Button(
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(containerColor = TertiaryColor),
                onClick = {
                    // Lanzar la intención para tomar la foto
                    takePicture.launch(imageUri)
                }
            ) {
                Text("Tomar Foto")
            }
            imageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Imagen tomada"
                )
            }

            Spacer(modifier = Modifier.height(180.dp))



//boton para publicar el post

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
fun PreviewPostIncidenceScreen() {
    PostIncidenceScreen()
}
