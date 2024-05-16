package com.example.fixitb_frontend.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.api.ApiViewModel
import com.example.fixitb_frontend.models.Incidence
import com.example.fixitb_frontend.models.MyNavigationRoute
import com.example.fixitb_frontend.ui.composables.ComposableHeader
import com.example.fixitb_frontend.ui.composables.buttons.GoBackButton
import com.example.fixitb_frontend.ui.theme.BackColor1
import com.example.fixitb_frontend.ui.theme.BackColor2
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor2
import com.example.fixitb_frontend.ui.theme.TertiaryColor
import com.example.fixitb_frontend.ui.theme.rowdiesFontFamily
import com.example.fixitb_frontend.viewmodel.MainViewModel
import com.pixelcarrot.base64image.Base64Image
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import kotlin.io.encoding.ExperimentalEncodingApi



//// FUNCION PARA SUBIR INCIDENCIA ////
suspend fun postIncidence(incidence: Incidence, imageUri: Uri, context: Context, vm: MainViewModel): Incidence? {
    var toReturn : Incidence? = null
    if (imageUri != Uri.EMPTY) {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        Base64Image.encode(bitmap) { base64 ->
            incidence.image = base64.toString()

            vm.viewModelScope.launch {
                try {
                    val response = ApiViewModel.incidenceService.insertIncidence("Bearer " + CurrentUser.userToken, incidence)
                    if (response.isSuccessful) {
                        toReturn = response.body()
                        Log.d("POST INCIDENCE", "Subido correctamente")
                        vm.navController!!.navigate(MyNavigationRoute.INCIDENCES)
                    } else {
                        Log.e("POST INCIDENCE", "Respuesta no exitosa: ${response.code()}")
                        toReturn = null
                    }
                } catch (e: Exception) {
                    Log.e("POST INCIDENCE", "Error al llamar a la API: ${e.message}")
                    toReturn = null
                }
            }
        }
        return toReturn
    } else {
        vm.viewModelScope.launch {
            try {
                val response = ApiViewModel.incidenceService.insertIncidence("Bearer " + CurrentUser.userToken, incidence)
                if (response.isSuccessful) {
                    toReturn = response.body()
                    Log.d("POST INCIDENCE", "Subido correctamente")
                    vm.navController!!.navigate(MyNavigationRoute.INCIDENCES)
                } else {
                    Log.e("POST INCIDENCE", "Respuesta no exitosa: ${response.code()}")
                    toReturn = null
                }
            } catch (e: Exception) {
                Log.e("POST INCIDENCE", "Error al llamar a la API: ${e.message}")
                toReturn = null
            }
        }
        return toReturn
    }
}



@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun PostIncidenceScreen(navController: NavHostController? = null, vm : MainViewModel? = null) {
    val context = LocalContext.current
    var titleInputText by remember { mutableStateOf(TextFieldValue()) }
    var deviceCodeInputText by remember { mutableStateOf(TextFieldValue()) }
    val movistarFieldValue by remember(key1 = vm!!.movistarCodeVal) { mutableStateOf(vm.movistarCodeVal) }
    var descriptionInputText by remember { mutableStateOf(TextFieldValue()) }

    //// Llistat de dispositius ////
    val listDispositiu = listOf("Torre","Monitor","Teclat","Ratolí", "Altre")
    var selectedDevice by remember { mutableStateOf(listDispositiu[0]) }
    var isExpanded by remember { mutableStateOf(false) }

    //// Llistat d'aules del centre ////
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

    //// Comprobar que el codigo debarras contenga solo numeros ////
    if (!vm!!.movistarCodeVal.matches(Regex("[0-9]+")) && vm.movistarCodeVal != ""){
        vm.movistarCodeVal = ""
        Toast.makeText(context, "El codi ha de ser numèric", Toast.LENGTH_SHORT).show()
    }

    //// COLORES DE LOS TEXTFIELDS ////
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = SecondaryColor,
        unfocusedContainerColor = PrimaryColor,

        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White,

        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = PrimaryColor,
        cursorColor = TertiaryColor,

    )

    //// CREAR URI PARA IMAGEN ////
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file)

    //// CONCEDER PERMISO DE CAMARA ////
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){
    if (it)
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show() }

    //// INICIAR CARAMA ////
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()){
        vm.currentImageUri = uri
        navController!!.navigate(MyNavigationRoute.INCIDENCE_POST) }


    //// FORMULARIO ////
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        BackColor1,
                        BackColor2
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ComposableHeader("INCIDÈNCIA NOVA")
            Spacer(modifier = Modifier.height(20.dp))

            // TEXT FIELD - TITOL INCIDENCIA
            TextField(
                maxLines = 1,
                value = titleInputText,
                onValueChange = { titleInputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp)),
                label = { Text("Títol *") },
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                onValueChange = { vm.changeCode(it)},
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
                    .fillMaxHeight(0.25f)
                    .clip(shape = RoundedCornerShape(10.dp)),
                label = { Text("Descripció") },
                colors = textFieldColors
            )



            Row(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()) {

                    // BOTON - HACER FOTO DE LA INCIDENCIA ****************************************
                    FloatingActionButton(
                        onClick = {
                            val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(uri)
                            } else {
                                // Request camera permission if not granted
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }

                                  },
                        containerColor = SecondaryColor,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .align(Alignment.Start)
                            .width(80.dp)
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_photo_camera),
                            contentDescription = "EXIT",
                            Modifier.size(34.dp)
                        )
                    }

                    // BOTON - SELECCIONAR FOTO DE LA INCIDENCIA **********************************
//                    FloatingActionButton(
//                        onClick = {},
//                        containerColor = SecondaryColor,
//                        modifier = Modifier
//                            .padding(vertical = 10.dp)
//                            .align(Alignment.Start)
//                            .width(80.dp)
//                    ) {
//                        Image(
//                            painterResource(id = R.drawable.ic_perm_media),
//                            contentDescription = "EXIT",
//                            Modifier.size(34.dp)
//                        )
//                    }
                }

                Spacer(modifier = Modifier.width(15.dp))

                // BOX - MOSTRAR IMAGEN INCIDENCIA **********************************
                val stroke = Stroke(width = 010f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 40f), 0f)
                )
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .padding(vertical = 20.dp, horizontal = 10.dp)
                    .background(PrimaryColor.copy(alpha = 0.2f))
                    .drawBehind {
                        drawRoundRect(color = SecondaryColor, style = stroke)
                    }
                ) {
                    if (vm.currentImageUri == Uri.EMPTY){
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
                            painter = rememberImagePainter(vm.currentImageUri),
                            contentDescription = "EXIT",
                            Modifier.size(200.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))

            //// BOTON PARA VOLVER ATRAS Y ENVIAR INCIDENCIA ////
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                GoBackButton(navController = navController!!, destination = MyNavigationRoute.INCIDENCES)
                Button(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(15),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor,
                        contentColor = Color.White
                    ),
                    onClick = {
                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                        val currentDate = sdf.format(Date())
                        val newIncidence = Incidence(
                            id = 0,
                            device = selectedDevice,
                            image = null,
                            title = titleInputText.text,
                            description = descriptionInputText.text,
                            openDate = currentDate,
                            closeDate = "",
                            status = "obert",
                            classNum = selectedClass.split(" ")[1].toInt(),
                            userAssigned = "",
                            codeMain = deviceCodeInputText.text,
                            codeMovistar = if (vm.movistarCodeVal == "") null else vm.movistarCodeVal.toInt(),
                            userId = CurrentUser.user!!.id!!
                        )
                        if (!checkNullValues(newIncidence))
                            Toast.makeText(context, "Completa tots els camps", Toast.LENGTH_SHORT).show()
                        else{
                            vm.viewModelScope.launch {
                                postIncidence(
                                    newIncidence,
                                    vm.currentImageUri,
                                    context,
                                    vm
                                )
                            }
                        }

                    },

                ) {
                    Text(text = "ENVIAR", fontFamily = rowdiesFontFamily)
                }
            }

        }
            }
}

//// COMPROBAR QUE LOS CAMPOS NO ESTEN VACIOS //// (si NO hay campos nulos devuelve TRUE)
fun checkNullValues(Incidence: Incidence): Boolean {
    if (Incidence.device == "Torre" || Incidence.device == "Monitor" ) {
        return Incidence.title != "" && Incidence.codeMain != "" && Incidence.codeMovistar.toString() != ""
    }
    return Incidence.title != "" && Incidence.device != "" && Incidence.classNum != 0
}

@Preview
@Composable
fun PreviewPostIncidenceScreen() {
    PostIncidenceScreen()
}
