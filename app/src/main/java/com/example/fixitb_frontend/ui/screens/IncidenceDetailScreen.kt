//package com.example.fixitb_frontend.ui.screens
//
//import android.annotation.SuppressLint
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.foundation.layout.Box
//import androidx.compose.runtime.Composable
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.fixitb_frontend.R
//import com.example.fixitb_frontend.ui.theme.Blue1
//import com.example.fixitb_frontend.ui.theme.SecondaryColor
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import coil.compose.AsyncImage
//import com.example.fixitb_frontend.api.ApiViewModel
//import com.example.fixitb_frontend.models.Incidence
//import com.example.fixitb_frontend.models.MyNavigationRoute
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.MainScope
//import kotlinx.coroutines.launch
//
//
//
//
//@SuppressLint("CoroutineCreationDuringComposition")
//@Composable
//fun IncidenceDetailScreen(navController: NavController) {
//    val selectedIncidenceId = ApiViewModel.selectedIncidenceId.value
//    var incidence: Incidence? by remember { mutableStateOf(null) }
//    val currentRole = CurrentUser.user?.role
//
//    // Utiliza LaunchedEffect para hacer la llamada al servicio una vez que selectedIncidenceId cambie
//    LaunchedEffect(selectedIncidenceId) {
//        if (selectedIncidenceId != null) {
//            try {
//                val response = ApiViewModel.incidenceService.getIncidenceById(
//                    "Bearer " + CurrentUser.userToken.toString(),
//                    selectedIncidenceId
//                )
//                if (response.isSuccessful) {
//                    val incidenceResponse = response.body()
//                    if (incidenceResponse != null) {
//                        incidence = incidenceResponse
//                    } else {
//                        Log.d("MAAAAL", "ERROR: Incidencia nula")
//                    }
//                } else {
//                    Log.d("MAAAAL", "ERROR EN OBTENER LOS DETALLES ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.d("MAAAL CATCH", "ERRROR ${e.message}")
//            }
//        }
//    }
//
//
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .fillMaxHeight()
//            .background(color = Blue1.copy(alpha = 1.0f))
//    ) {
//        incidence?.let { incidence ->
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp), // Espacio alrededor para evitar bordes
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.Top
//            ) {
//                Spacer(modifier = Modifier.height(20.dp))
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Título de la pantalla
//
//                    // Imagen de la empresa
//                    Image(
//                        modifier = Modifier.size(75.dp),
//                        painter = painterResource(id = R.drawable.img_logo_1),
//                        contentDescription = null
//                    )
//                }
//                //numero incidencia
//                Column {
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Text(
//                        text = "Incidencia ${incidence.id}",
//                        color = Color.White,
//                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
//                    )
//                    Text(
//                        text = incidence.title,
//                        color = Color.White,
//                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
//                    )
//                }
//                Spacer(modifier = Modifier.height(20.dp))
//                //box con los detalles
//                // muestra las fechas
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(
//                                SecondaryColor.copy(alpha = 0.4f),
//                                shape = RoundedCornerShape(5.dp)
//                            )
//                            .padding(2.dp)
//                    ) {
//                        Text(
//                            text = "Open Date: ${incidence.openDate}",
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(8.dp)
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(8.dp)) // Espacio entre las cajas
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(
//                                SecondaryColor.copy(alpha = 0.4f),
//                                shape = RoundedCornerShape(5.dp)
//                            )
//                            .padding(2.dp)
//                    ) {
//                        Text(
//                            text = "Close Date: ${incidence.closeDate}",
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(8.dp)
//                        )
//                    }
//                }
//                Spacer(modifier = Modifier.height(20.dp))
//                // Muestra todos los datos
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(
//                            SecondaryColor.copy(alpha = 0.4f),
//                            shape = RoundedCornerShape(5.dp)
//                        )
//                        .padding(8.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        // Obtener la incidencia deseada
//                        // Mostrar los campos de la incidencia
//                        Text(text = "Aula: ${incidence.classNum}", color = Color.White)
//                        Text(text = "Dispositiu: ${incidence.device}", color = Color.White)
//                        Text(text = "Codi: ${incidence.codeMain}", color = Color.White)
//                        Text(text = "Codi Movistar: ${incidence.codeMovistar}", color = Color.White)
//                        Text(text = "Descripció: ${incidence.description}", color = Color.White)
//                        Text(text = "Imatge: ", color = Color.White)
//                        AsyncImage(model = incidence.image, contentDescription = incidence.device)
//                    }
//
//                }
//                Spacer(modifier = Modifier.height(20.dp))
//                // Muestra el estado y el tecnico
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(
//                                SecondaryColor.copy(alpha = 0.4f),
//                                shape = RoundedCornerShape(5.dp)
//                            )
//                            .padding(2.dp)
//                    ) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Text(
//                                text = "Estat: ${incidence.status}",
//                                color = Color.White,
//                                modifier = Modifier.padding(8.dp)
//                            )
//                            if (currentRole == "student"){
//                                Box(
//                                    modifier = Modifier
//                                        .size(20.dp)
//                                        .background(
//                                            shape = RoundedCornerShape(10.dp),
//                                            color = if (incidence.status == "obert")
//                                                Color.Green
//                                            else if (incidence.status == "tancat")
//                                                Color.Red
//                                            else
//                                                Color.Yellow
//                                        )
//                                )
//                            } else{
//                                var selectedStatus by remember { mutableStateOf(incidence.status) }
//                                val statusOptions = listOf("obert", "tancat", "en proces")
//                                var isDropdownExpanded by remember { mutableStateOf(false) }
//                                Box(
//                                    modifier = Modifier
//                                        .size(20.dp)
//                                        .background(
//                                            shape = RoundedCornerShape(10.dp),
//                                            color = when (selectedStatus) {
//                                                "obert" -> Color.Green
//                                                "tancat" -> Color.Red
//                                                else -> Color.Yellow
//                                            }
//                                        )
//                                        .clickable {
//                                            isDropdownExpanded = !isDropdownExpanded
//                                        }
//                                )
//                                {
//                                    DropdownMenu(isDropdownExpanded, onDismissRequest = { isDropdownExpanded = false }) {
//                                        statusOptions.forEachIndexed { index, option ->
//                                            DropdownMenuItem(
//                                                text = { Text(text = option) },
//                                                onClick = {
//                                                    selectedStatus = option
//                                                    isDropdownExpanded = false
//                                                    val incidenceUpdate = Incidence(
//                                                        incidence.id,
//                                                        incidence.device,
//                                                        incidence.image,
//                                                        incidence.title,
//                                                        incidence.description,
//                                                        incidence.openDate,
//                                                        incidence.closeDate,
//                                                        selectedStatus,
//                                                        incidence.classNum,
//                                                        CurrentUser.user?.email,
//                                                        incidence.codeMain,
//                                                        incidence.codeMovistar,
//                                                        incidence.userId
//                                                    )
//                                                    CoroutineScope(Dispatchers.IO).launch {
//                                                        incidenceUpdate?.let {
//                                                            try {
//                                                                val response = ApiViewModel.incidenceService.updateIncidenceById(
//                                                                    "Bearer " + CurrentUser.userToken,
//                                                                    incidence.id!!.toInt(),
//                                                                    incidenceUpdate
//                                                                )
//                                                                if (response.isSuccessful) {
//                                                                    MainScope().launch {
//                                                                        navController.navigate(MyNavigationRoute.INCIDENCES)
//                                                                    }
//                                                                } else {
//                                                                    Log.d("Ñ UPDATE STATUSSS", "ERROR AL ACTUALIZAR LA INCIDENCIA: ${response.message()}")
//                                                                    Log.d("Ñ RECORRE", incidenceUpdate.toString())
//                                                                }
//                                                            } catch (e: Exception) {
//                                                                Log.d("Ñ MAAAAL", "ERRORrr AL ACTUALIZAR LA INCIDENCIA: ${e.message}")
//                                                                Log.d("Ñ RECORRE", incidenceUpdate.toString())
//                                                            }
//                                                        }
//                                                    }
//                                                })
//                                        }
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    Spacer(modifier = Modifier.width(8.dp)) // Espacio entre las cajas
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(
//                                SecondaryColor.copy(alpha = 0.4f),
//                                shape = RoundedCornerShape(5.dp)
//                            )
//                            .padding(2.dp)
//                    ) {
//                        if (currentRole == "student") {
//                            if (incidence.userAssigned != null) {
//                                Text(
//                                    text = "Tecnic Assignat: ${incidence.userAssigned}",
//                                    color = Color.White,
//                                    modifier = Modifier.padding(8.dp)
//                                )
//                            } else {
//                                Text(
//                                    text = "Tecnic Assignat: No hi ha técnic",
//                                    color = Color.White,
//                                    modifier = Modifier.padding(8.dp)
//                                )
//                            }
//
//                        } else if (currentRole == "tecnic") {
//                            if (incidence.userAssigned == null) {
//                                val incidenceUpdate = Incidence(
//                                    incidence.id,
//                                    incidence.device,
//                                    incidence.image,
//                                    incidence.title,
//                                    incidence.description,
//                                    incidence.openDate,
//                                    incidence.closeDate,
//                                    "En proces",
//                                    incidence.classNum,
//                                    CurrentUser.user?.email,
//                                    incidence.codeMain,
//                                    incidence.codeMovistar,
//                                    incidence.userId
//                                )
//                                Button(onClick = {
//                                    CoroutineScope(Dispatchers.IO).launch {
//                                        incidenceUpdate?.let {
//                                            try {
//                                                val response = ApiViewModel.incidenceService.updateIncidenceById(
//                                                    "Bearer " + CurrentUser.userToken,
//                                                    incidence.id!!.toInt(),
//                                                    incidenceUpdate
//                                                )
//                                                if (response.isSuccessful) {
//                                                    MainScope().launch {
//                                                        navController.navigate(MyNavigationRoute.INCIDENCES)
//                                                    }
//                                                } else {
//                                                    Log.d("Ñ MAAAAL", "ERROR AL ACTUALIZAR LA INCIDENCIA: ${response.message()}")
//                                                    Log.d("Ñ RECORRE", incidenceUpdate.toString())
//                                                }
//                                            } catch (e: Exception) {
//                                                Log.d("Ñ MAAAAL", "ERRORrr AL ACTUALIZAR LA INCIDENCIA: ${e.message}")
//                                                Log.d("Ñ RECORRE", incidenceUpdate.toString())
//                                            }
//                                        }
//                                    }
//
//                                }){
//                                    Text("Asignar incidencia")
//                                }
//                            } else{
//                                Text(
//                                    text = "Tecnic Assignat: ${incidence.userAssigned}",
//                                    color = Color.White,
//                                    modifier = Modifier.padding(8.dp)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
