package com.example.fixitb_frontend.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.models.MyNavigationRoute
import com.example.fixitb_frontend.ui.composables.ComposableHeader
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor2
import com.example.fixitb_frontend.ui.theme.TertiaryColor
import com.example.fixitb_frontend.ui.theme.rowdiesFontFamily
import com.example.fixitb_frontend.viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostIncidenceScreen(navController: NavHostController? = null, vm : MainViewModel? = null) {
    var deviceInputText by remember { mutableStateOf("Torre") }
    var classInputText by remember { mutableStateOf("Aula 001") }
    var deviceCodeInputText by remember { mutableStateOf(TextFieldValue()) }
    var movistarFieldValue by remember(key1 = vm!!.movistarCodeVal) { mutableStateOf(vm.movistarCodeVal) }
    var descriptionInputText by remember { mutableStateOf(TextFieldValue()) }

    var currentImage by remember { mutableStateOf(String()) }


    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = SecondaryColor,
        unfocusedContainerColor = SecondaryColor2,

        focusedIndicatorColor = Color.White,
        focusedLabelColor = Color.White,
        cursorColor = TertiaryColor,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blue1.copy(alpha = 1.0f))    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Espacio alrededor para evitar bordes
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ComposableHeader("INCIDÈNCIA NOVA")

            Spacer(modifier = Modifier.height(20.dp))

            // Llistat de dispositius
            val listDispositiu = listOf("Torre","Monitor","Teclat","Ratolí", "Altre")
            var selectedDevice by remember { mutableStateOf(listDispositiu[0]) }
            var isExpanded by remember { mutableStateOf(false) }

            Row {

                // DROP DOWN BOX - DISPOSITIU
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(12.dp))
                ) {
                    ExposedDropdownMenuBox(
                        expanded = isExpanded ,
                        onExpandedChange = {isExpanded = !isExpanded}
                    ) {
                        TextField(
                            modifier = Modifier.menuAnchor(),
                            value = selectedDevice ,
                            onValueChange = {},
                            label = { Text("Dispositiu *") } ,
                            readOnly = true,
                            colors = textFieldColors,
                            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)},

                        )
                        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = {isExpanded = false}) {
                            listDispositiu.forEachIndexed{ index, text ->
                                DropdownMenuItem(
                                    text = { Text(text = text) },
                                    onClick = {
                                        selectedDevice = listDispositiu[index]
                                        isExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(10.dp))

                // Llistat d'aules del centre
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
                var selectedClass by remember { mutableStateOf(listAula[0]) }
                var isExpandedAula by remember { mutableStateOf(false) }

                // DROP DOWN BOX - AULA
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(10.dp))
                ) {
                    ExposedDropdownMenuBox(
                        expanded = isExpandedAula ,
                        onExpandedChange = {isExpandedAula = !isExpandedAula}
                    ) {
                        TextField(
                            modifier = Modifier.menuAnchor(),
                            value = selectedClass ,
                            onValueChange = {},
                            label = { Text("Aula *") } ,
                            readOnly = true,
                            colors = textFieldColors,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedAula)}
                        )
                        ExposedDropdownMenu(expanded = isExpandedAula, onDismissRequest = {isExpandedAula = false}) {
                            listAula.forEachIndexed{ index, text ->
                                DropdownMenuItem(
                                    text = { Text(text = text) },
                                    onClick = {
                                        selectedClass = listAula[index]
                                        isExpandedAula = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(10.dp))

            // TEXT FIELD - CODI DISPOSITIU
            TextField(
                maxLines = 1,
                value = deviceCodeInputText,
                onValueChange = { deviceCodeInputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp)),
                label = { Text("Codi Dispositiu") },
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(10.dp))

            // TEXT FIELD - CODI MOVISTAR
            TextField(
                maxLines = 1,
                value = movistarFieldValue,
                onValueChange = { vm!!.changeCode(it)},
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp)),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                trailingIcon = {
                    IconButton(onClick = {navController!!.navigate(MyNavigationRoute.CAMERA_BARCODE)},
                        modifier = Modifier
                            .size(24.dp)
                            ) {
                        Image(
                            painter = painterResource(R.drawable.ic_photo_camera),
                            contentDescription = "EXIT",
                            Modifier.size(24.dp)
                        )
                    }
                },
                label = { Text("Codi Movistar") }
            )
            
            Spacer(modifier = Modifier.height(10.dp))

            // TEXT FIELD - DESCRIPCIÓ
            TextField(
                maxLines = 5,
                value = descriptionInputText,
                onValueChange = { descriptionInputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .clip(shape = RoundedCornerShape(10.dp)),
                label = { Text("Descripció") },
                colors = textFieldColors
            )


//            Spacer(modifier = Modifier.fillMaxHeight(0.15f))

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()) {

                    // BOTON - HACER FOTO DE LA INCIDENCIA ****************************************
                    FloatingActionButton(
                        onClick = {},
                        containerColor = SecondaryColor,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .align(Alignment.Start)
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_photo_camera),
                            contentDescription = "EXIT",
                            Modifier.size(34.dp)
                        )
                    }

                    // BOTON - SELECCIONAR FOTO DE LA INCIDENCIA **********************************
                    FloatingActionButton(
                        onClick = {},
                        containerColor = SecondaryColor,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .align(Alignment.Start)
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_perm_media),
                            contentDescription = "EXIT",
                            Modifier.size(34.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(15.dp))

                // BOX - MOSTRAR IMAGEN INCIDENCIA **********************************
                val stroke = Stroke(width = 010f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 40f), 0f)
                )
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.9f)
                    .padding(10.dp)
                    .background(SecondaryColor2.copy(alpha = 0.3f))
                    .drawBehind {
                        drawRoundRect(color = SecondaryColor, style = stroke)
                    }
                ) {
                    if (currentImage.isEmpty()){
                        Column(modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()) {
                            Text(
                                "Imatge incidencia",
                                fontSize = 20.sp,
                                color = SecondaryColor.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth())
                            Text(
                                "Formats acceptats: .jpg, .png, .jpeg",
                                fontSize = 10.sp,
                                color = SecondaryColor.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth())
                        }
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_perm_media),
                            contentDescription = "EXIT",
                            Modifier.size(200.dp)
                        )
                    }
                }
            }
//            Spacer(modifier = Modifier.fillMaxHeight(0.5f))
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                shape = RoundedCornerShape(15),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                    contentColor = Color.White
                ),
            ) {
                Text(text = "ENVIAR", fontFamily = rowdiesFontFamily)
            }
        }
            }

}

@Preview
@Composable
fun PreviewPostIncidenceScreen() {
    PostIncidenceScreen()
}
