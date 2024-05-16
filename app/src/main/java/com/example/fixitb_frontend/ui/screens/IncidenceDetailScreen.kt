package com.example.fixitb_frontend.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fixitb_frontend.api.ApiViewModel
import com.example.fixitb_frontend.models.Incidence
import com.example.fixitb_frontend.models.MyNavigationRoute
import com.example.fixitb_frontend.models.User
import com.example.fixitb_frontend.ui.composables.ComposableBoldText1
import com.example.fixitb_frontend.ui.composables.ComposableBoldText2
import com.example.fixitb_frontend.ui.composables.ComposableHeader
import com.example.fixitb_frontend.ui.composables.ComposableNormalText2
import com.example.fixitb_frontend.ui.composables.ComposableNormalText3
import com.example.fixitb_frontend.ui.composables.buttons.GoBackButton
import com.example.fixitb_frontend.ui.screens.IncidenceDetails.currentIncidence
import com.example.fixitb_frontend.ui.screens.IncidenceDetails.isSheetOpen
import com.example.fixitb_frontend.ui.theme.BackColor1
import com.example.fixitb_frontend.ui.theme.BackColor2
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.TertiaryColor
import com.example.fixitb_frontend.viewmodel.MainViewModel
import com.example.fixitb_frontend.viewmodel.MainViewModel.navController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


object IncidenceDetails {
    var isSheetOpen by mutableStateOf(false)
    var currentIncidence: Incidence? by mutableStateOf(null)
}

//// FUNCION CARGAR LISTA DE TÉCNICOS ////
suspend fun getTecnics(): List<User> {
    val response = ApiViewModel.userService.getUsers("Bearer " + CurrentUser.userToken)

    if (response.isSuccessful) {
        val users = response.body()
        if (users != null) {
            return users.filter{it.role.lowercase() == "tecnic"}
        } else {
            Log.e("USERS", "Response body is null")
            return emptyList()
        }
    } else {
        Log.e("USERS", "Unsuccessful response: ${response.code()}")
        return emptyList()
    }
}

//// FUNCION PARA ELIMINAR INCIDENCIA ////
suspend fun deleteIncidence() {
    val selectedIncidenceId = ApiViewModel.selectedIncidenceId.value
    try {
        val response = ApiViewModel.incidenceService.deleteIncidenceById(
            "Bearer " + CurrentUser.userToken,
            selectedIncidenceId!!.toInt()
        )
        if (response.isSuccessful) {
            Log.d("IDINCIDENCE", selectedIncidenceId.toString())
            navController!!.navigate(MyNavigationRoute.INCIDENCES)
        } else Log.e("DELETE_INCIDENCE", "Error en la eliminación de la incidencia: ${response.message()}")
    } catch (e: Exception) {
        Log.e("DELETE_INCIDENCE", "Error al eliminar la incidencia: ${e.message}")
    }
}

//// ITEM TÉCNICO ////
@Composable
fun tecnicListItem(user: User){
    Box(modifier = Modifier
        .padding(vertical = 4.dp, horizontal = 10.dp)
        .height(50.dp)
        .fillMaxSize()
        .background(
            PrimaryColor, shape = RoundedCornerShape(12.dp)
        )
        .clickable {
            isSheetOpen = false
            navController!!.navigate(MyNavigationRoute.INCIDENCE_DETAILS)
        },
        ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 5.dp),horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Text(text = user.email, color = Color.White, modifier = Modifier.fillMaxWidth(0.65f))
            Button(modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = TertiaryColor.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    isSheetOpen = false
                    updateIncidence(null, null, user.email, navController!!.context)
                }) {
                Text(text = "Assignar")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun IncidenceDetailScreen(navController: NavController) {
    val selectedIncidenceId = ApiViewModel.selectedIncidenceId.value
    val currentRole = CurrentUser.user?.role

    val sheetState = rememberModalBottomSheetState()

    val tecnicsListState = remember { mutableStateOf<List<User>>(emptyList()) }
    var tecnicsList = listOf<User>()

    val coroutineScope = rememberCoroutineScope()
    SideEffect {
        coroutineScope.launch {
            tecnicsList = getTecnics()
            tecnicsListState.value = tecnicsList
        }
    }

    //// BOTTOM SHEET - LISTA DE TECNICOS ////
    if (isSheetOpen){
        ModalBottomSheet(
            modifier = Modifier.fillMaxSize(),
            sheetState = sheetState,
            containerColor = SecondaryColor.copy(alpha = 0.9f),
            onDismissRequest = { isSheetOpen = false }
        ) {
                LazyColumn(
                    content = {
                    items(
                        items = tecnicsListState.value,
                        itemContent = {
                            tecnicListItem(it)})
                })
        }
    }


    // Utiliza LaunchedEffect para hacer la llamada al servicio una vez que selectedIncidenceId cambie
    LaunchedEffect(selectedIncidenceId) {
        if (selectedIncidenceId != null) {
            try {
                val response = ApiViewModel.incidenceService.getIncidenceById("Bearer " + CurrentUser.userToken.toString(), selectedIncidenceId)
                if (response.isSuccessful) {
                    val incidenceResponse = response.body()
                    if (incidenceResponse != null) {
                        currentIncidence = incidenceResponse
                    } else {
                        Log.d("MAAAAL", "ERROR: Incidencia nula")
                    }
                } else {
                    Log.d("MAAAAL", "ERROR EN OBTENER LOS DETALLES ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("MAAAL CATCH", "ERRROR ${e.message}")
            }
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        BackColor1,
                        BackColor2
                    )
                )
            )
    ) {
        currentIncidence?.let { incidence ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                //// TITULO Y NUM. INCIDENCIA ////
                Column(modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)) {
                    Spacer(modifier = Modifier.height(10.dp))
                    ComposableBoldText2(text = incidence.title, fontSize = 30)
                    ComposableNormalText2(text = "Incidència #${incidence.id}", fontSize = 20, customColor = Color(0xFFFFFFFF).copy(alpha = 0.75f))
                }

                //// FECHAS ////
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                ) {
                    //// FECHA DE CREACION INCIDENCIA ////
                        Column(modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight()
                            .background(
                                PrimaryColor,
                                shape = RoundedCornerShape(10.dp)
                            ), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            ComposableBoldText2(text = "Data creació: ", fontSize = 16, true)
                            ComposableNormalText2(text = incidence.openDate, fontSize = 14, true)

                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    //// FECHA DE CIERRE INCIDENCIA ////
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(
                            PrimaryColor,
                            shape = RoundedCornerShape(10.dp)
                        ), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        ComposableBoldText2(text = "Data tancament: ", fontSize = 16, true)
                        ComposableNormalText2(text = incidence.closeDate.toString(), fontSize = 14, true)

                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                //// DETALLES DE LA INCIDENCIA ////
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .background(
                            PrimaryColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row {
                            ComposableBoldText2(text = "Nº Aula: ", 15)
                            ComposableNormalText2(text = incidence.classNum.toString(), fontSize = 15)
                        }
                        Row {
                            ComposableBoldText2(text = "Dispositiu:  ", 15)
                            ComposableNormalText2(text = incidence.device, fontSize = 15)
                        }
                        Row {
                            ComposableBoldText2(text = "Codi: ", 15)
                            ComposableNormalText2(text = incidence.codeMain, fontSize = 15)
                        }
                        Row {
                            ComposableBoldText2(text = "Codi Movistar: ", 15)
                            ComposableNormalText2(text = incidence.codeMovistar.toString(), fontSize = 15)
                        }
                        Row {
                            ComposableBoldText2(text = "Descripció: ", 15)
                            ComposableNormalText2(text = incidence.description.toString(), fontSize = 15)
                        }
                        ComposableBoldText2(text = "Imatge: ", 15)
                        AsyncImage(model = incidence.image, contentDescription = incidence.device)
                    }

                }
                Spacer(modifier = Modifier.height(10.dp))

                var backgroundColor = when (currentIncidence?.status) {
                    "obert" -> Color(0xFFCCFF90)
                    "tancat" -> Color(0xFFFF8A80)
                    else -> Color(0xFFFFE57F)
                }

                //// ESTADO Y TECNICO ASIGNADO ////
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth(0.3f)
                            .background(
                                PrimaryColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(2.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxHeight()
                            .fillMaxWidth()) {
                            ComposableBoldText2(text = "Estat:", fontSize = 14)
                                    var selectedStatus by remember { mutableStateOf(incidence.status) }
                                    val statusOptions = listOf("obert", "tancat", "en proces")
                                    var isDropdownExpanded by remember { mutableStateOf(false) }
                                    Box(modifier = Modifier
                                        .padding(top = 5.dp)
                                        .height(40.dp)
                                        .fillMaxWidth(0.8f)
                                        .align(Alignment.CenterHorizontally)
                                        .background(
                                            shape = RoundedCornerShape(10.dp),
                                            color = backgroundColor
                                        )
                                        .clickable {
                                            if (currentRole != "student")
                                                isDropdownExpanded = !isDropdownExpanded
                                        }
                                    )
                                    {
                                        Text(text =incidence.status, modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.Center), color = PrimaryColor, textAlign = TextAlign.Center)
                                        DropdownMenu(isDropdownExpanded, onDismissRequest = { isDropdownExpanded = false }) {
                                            statusOptions.forEachIndexed { _, option ->
                                                DropdownMenuItem(
                                                    text = { Text(text = option) },
                                                    onClick = {
                                                        selectedStatus = option
                                                        isDropdownExpanded = false
                                                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                                                        val currentDate = sdf.format(Date())
                                                        val closeDate = if (selectedStatus == "tancat") currentDate else ""
                                                        updateIncidence(selectedStatus, closeDate, null, navController.context)
                                                    })
                                            }
                                        }

                                }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(0.5f)
                            .background(
                                PrimaryColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(2.dp)
                            .clickable { if (currentRole == "admin") isSheetOpen = true }
                    ) {
                        if (currentRole == "student") {
                            if (incidence.userAssigned != null) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    ComposableBoldText2(text = "Tècnic assignat:", fontSize = 15)
                                    ComposableNormalText2(text = incidence.userAssigned, fontSize = 14)
                                }

                            } else {
                                Text(
                                    text = "Tecnic Assignat: No hi ha técnic",
                                    color = Color.White,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                        } else if (currentRole != "student"){
                            if (incidence.userAssigned == null || incidence.userAssigned == "") {
                                Button(
                                    onClick = {
                                    updateIncidence("en proces", null, CurrentUser.user?.email!!, navController.context)
                                }){
                                    Text("Asignar Incidència")
                                }
                            } else{
                                Column(modifier = Modifier.padding(8.dp)) {
                                    ComposableBoldText2(text = "Tècnic assignat:", fontSize = 15)
                                    ComposableNormalText2(text = incidence.userAssigned, fontSize = 14)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                    GoBackButton(navController = navController, destination = MyNavigationRoute.INCIDENCES)
                    val showDialog = remember { mutableStateOf(false) }

                    if (showDialog.value) {
                        AlertDialog(
                            onDismissRequest = {
                                showDialog.value = false
                            },
                            title = {
                                Text(text = "Eliminar Incidencia")
                            },
                            text = {
                                Text(text = "¿Está seguro de que desea eliminar esta incidencia?")
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            deleteIncidence()
                                            showDialog.value = false
                                        }

                                    }
                                ) {
                                    Text(text = "Aceptar")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        showDialog.value = false
                                    }
                                ) {
                                    Text(text = "Cancelar")
                                }
                            }
                        )

                    }
                    Button(

                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(0.48f)
                            .height(50.dp),
                        shape = RoundedCornerShape(15),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF7E73).copy(alpha = 0.8f),
                            contentColor = Color.White
                        ),
                        onClick = { showDialog.value = true }) {
                        ComposableBoldText2(text = "BORRAR", fontSize = 17)
                    }
                    Button(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(15),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3FC766).copy(alpha = 0.8f),
                            contentColor = Color.White
                        ),
                        onClick = { /* FUNCION EDITAR INCIDENCIA*/ }) {
                        ComposableBoldText2(text = "EDITAR", fontSize = 17)
                    }
                }
            }
        }
    }
}


fun updateIncidence(status: String?, closeDate: String?, userAssigned: String?, context: Context){
    val newIncidence = Incidence(
        currentIncidence!!.id,
        currentIncidence!!.device,
        currentIncidence!!.image,
        currentIncidence!!.title,
        currentIncidence!!.description,
        currentIncidence!!.openDate,
        if (closeDate != null) closeDate else currentIncidence!!.closeDate,
        if (status != null && status != "") status else currentIncidence!!.status,
        currentIncidence!!.classNum,
        if (userAssigned != null && userAssigned != "") userAssigned else currentIncidence!!.userAssigned,
        currentIncidence!!.codeMain,
        currentIncidence!!.codeMovistar,
        currentIncidence!!.userId
    )

    CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiViewModel.incidenceService.updateIncidenceById(
                    "Bearer " + CurrentUser.userToken,
                    newIncidence.id!!.toInt(),
                    newIncidence
                )
                if (response.isSuccessful) {
                    MainScope().launch {
                        currentIncidence = newIncidence
                        Toast.makeText(context, "Incidència actualitzada", Toast.LENGTH_SHORT).show()
//                        navController!!.navigate(MyNavigationRoute.INCIDENCES)
                    }
                } else {
                    Log.d("Ñ UPDATE STATUSSS", "ERROR AL ACTUALIZAR LA INCIDENCIA: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("Ñ MAAAAL", "ERRORrr AL ACTUALIZAR LA INCIDENCIA: ${e.message}")
            }
    }
}
