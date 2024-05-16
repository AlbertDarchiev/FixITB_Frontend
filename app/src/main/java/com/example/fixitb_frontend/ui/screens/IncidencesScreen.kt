package com.example.fixitb_frontend.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.api.ApiViewModel
import com.example.fixitb_frontend.models.Incidence
import com.example.fixitb_frontend.models.MyNavigationRoute
import com.example.fixitb_frontend.ui.composables.ComposableBoldText2
import com.example.fixitb_frontend.ui.composables.ComposableHeader
import com.example.fixitb_frontend.ui.composables.ComposableNormalText2
import com.example.fixitb_frontend.ui.composables.ComposableNormalText3
import com.example.fixitb_frontend.ui.theme.BackColor1
import com.example.fixitb_frontend.ui.theme.BackColor2
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.example.fixitb_frontend.ui.theme.TertiaryColor
import com.example.fixitb_frontend.ui.theme.White2
import com.example.fixitb_frontend.ui.theme.rowdiesFontFamily
import com.example.fixitb_frontend.viewmodel.MainViewModel
import kotlinx.coroutines.launch

//// OBTENER INCIDENCIAS DE API////
suspend fun getIncidences(): List<Incidence> {
    val response = if (CurrentUser.user!!.role == "student")
        ApiViewModel.incidenceService.getIncidencesForUser(CurrentUser.user!!.id ,"Bearer " + CurrentUser.userToken)
    else
        ApiViewModel.incidenceService.getIncidences("Bearer " + CurrentUser.userToken)
    if (response.isSuccessful) {
        val incidences = response.body()
        if (incidences != null) {
            return incidences
        } else {
            Log.e("INCIDENCES", "Response body is null")
            return emptyList()
        }
    } else {
        Log.e("INCIDENCES", "Unsuccessful response: ${response.code()}")
        return emptyList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun IncidencesScreen(
    navController: NavHostController,
) {
    val coroutineScope = rememberCoroutineScope()

    //// LISTAS DE FILTROS INCIDENCIAS ////
    var openIncidences = remember { mutableStateOf<List<Incidence>>(emptyList()) }
    var processIncidences = remember { mutableStateOf<List<Incidence>>(emptyList()) }
    var closedIncidences = remember { mutableStateOf<List<Incidence>>(emptyList()) }
    var AssignedIncidences = remember { mutableStateOf<List<Incidence>>(emptyList()) }
    var UnsignedIncidences = remember { mutableStateOf<List<Incidence>>(emptyList()) }

    //// ESTADOS DE LOS FILTROS DE INCIDENCIAS ////
    val filterOpenState = remember { mutableStateOf(false) }
    val filterProcessState = remember { mutableStateOf(false) }
    val filterClosedState = remember { mutableStateOf(false) }
    val filterAssignedState = remember { mutableStateOf(false) }
    val filterUnsignedState = remember { mutableStateOf(false) }

    var filteredIncidences = remember { mutableStateOf<List<Incidence>>(emptyList()) }

    //// ACTUALIZAR LISTA DE INCIDENCIAS (segun que filtros esten activos)////
    fun filterIncidences(): List<Incidence> {
        val filteredList = mutableListOf<Incidence>()

        if (filterOpenState.value)
            filteredList.addAll(openIncidences.value)
        if (filterProcessState.value)
            filteredList.addAll(processIncidences.value)
        if (filterClosedState.value)
            filteredList.addAll(closedIncidences.value)

        if (!filterOpenState.value && !filterProcessState.value && !filterClosedState.value && !filterAssignedState.value && !filterUnsignedState.value) {
            filteredList.addAll(openIncidences.value)
            filteredList.addAll(processIncidences.value)
            filteredList.addAll(closedIncidences.value)
        }
        if (filterAssignedState.value)
            if (filterClosedState.value || filterOpenState.value || filterProcessState.value || filterUnsignedState.value)
                return filteredList.distinct().filter { it.userAssigned != null && it.userAssigned != "" }
            else filteredList.addAll(AssignedIncidences.value)
        else if (filterUnsignedState.value)
            if (filterClosedState.value || filterOpenState.value || filterProcessState.value || filterAssignedState.value)
                return filteredList.distinct().filter { it.userAssigned == null || it.userAssigned == "" }
            else filteredList.addAll(UnsignedIncidences.value)

        else if (filterAssignedState.value && filterUnsignedState.value) {
            filteredList.addAll(AssignedIncidences.value)
            filteredList.addAll(UnsignedIncidences.value)
            return filteredList.distinct()
        }

        return filteredList.distinct()
    }

    SideEffect {
        coroutineScope.launch {
            val allIncidences = getIncidences()
            openIncidences.value = allIncidences.filter { it.status == "obert" }
            processIncidences.value = allIncidences.filter { it.status == "en proces" }
            closedIncidences.value = allIncidences.filter { it.status == "tancat" }
            AssignedIncidences.value = allIncidences.filter { it.userAssigned != null && it.userAssigned != "" }
            UnsignedIncidences.value = allIncidences.filter { it.userAssigned == null || it.userAssigned == "" }
            if (CurrentUser.user!!.role == "student")
                filteredIncidences.value = filterIncidences().distinct().filter { it.userId == CurrentUser.user!!.id }
            else filteredIncidences.value = filterIncidences().distinct()
        }
    }

    //// ITEM FILTER CHIP ////
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun filterChip(name: String, selected: MutableState<Boolean>) {
        ElevatedFilterChip(
            modifier = Modifier.padding(horizontal = 5.dp),
            selected = selected.value,
            onClick = { selected.value = !selected.value
                filteredIncidences.value = filterIncidences()},
            label = { Text(name) },
            colors = FilterChipDefaults.filterChipColors(containerColor = SecondaryColor, selectedContainerColor = PrimaryColor, labelColor = Color.White, selectedLabelColor = Color.White),

        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    BackColor1,
                    BackColor2
                )
            )
        )
        )
    {
        //// HEADER ////
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 30.dp, bottom =20.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(), ) {
                ComposableHeader(text = "INCIDÈNCIES", width = 0.8f)
                IconButton(onClick = {navController.navigate(MyNavigationRoute.USER_DETAILS)},
                    modifier = Modifier
                        .padding(5.dp)
                        .background(SecondaryColor, shape = RoundedCornerShape(12.dp))
                        ) {
                    Image(
                        painter = painterResource(R.drawable.ic_person),
                        contentDescription = "EXIT",
                        Modifier.size(24.dp)
                    )
                }
            }

            //// LISTA DE FILTROS ////
            if(CurrentUser.user!!.role != "student"){
                LazyRow() {
                    items(
                        items = listOf(
                            "Oberts" to filterOpenState,
                            "En proces" to filterProcessState,
                            "Tancats" to filterClosedState,
                            "Assignats" to filterAssignedState,
                            "Sense assignar" to filterUnsignedState
                        )
                    ) { (name, selectedState) ->
                        filterChip(name, selectedState)
                    }
                }
            }

            //// LISTA DE INCIDENCIAS ////
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxHeight(1f),
            ) {
                items(
                    items = filteredIncidences.value,
                    itemContent = {
                        incidenceListItem(it)})
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
        if (CurrentUser.user!!.role == "student") {
            Column(modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate(MyNavigationRoute.INCIDENCE_POST)},
                    containerColor = SecondaryColor,
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(CircleShape)
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_add),
                        contentDescription = "EXIT",
                        Modifier.size(34.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

        }

    }
}



//// ITEM INCIDENCIA ////
@Composable
fun incidenceListItem(incidence: Incidence) {
    val closedColor = Color(0xFFFF8A80)
    val processColor = Color(0xFFFFE57F)
    val openColor = Color(0xFFCCFF90)
    val backColor = Color(0xFF0B1C38)

    Box(modifier = Modifier
        .padding(vertical = 6.dp)
        .fillMaxSize()
        .background(
            PrimaryColor, shape = RoundedCornerShape(12.dp)
        )
        .clickable {
            ApiViewModel.setSelectedIncidenceId(incidence.id!!)
            MainViewModel.navController!!.navigate(MyNavigationRoute.INCIDENCE_DETAILS)
        }
    ) {
        Row {
            Column(modifier = Modifier.padding(8.dp)) {
                ComposableBoldText2(text = incidence.title, fontSize = 16)
                ComposableNormalText2(text = "Aula ${incidence.classNum} · ${incidence.device}", fontSize = 14)
                Row {
                    ComposableNormalText2(text = incidence.openDate + " - ", fontSize = 14)
                    if (incidence.closeDate != null && incidence.closeDate != "")
                        ComposableNormalText2(text = incidence.closeDate, fontSize = 14)
                    Spacer(modifier = Modifier.weight(1f))
                }
                if (CurrentUser.user!!.role != "student")
                    if(incidence.userAssigned != null && incidence.userAssigned != "") ComposableNormalText2(text = incidence.userAssigned, fontSize = 14, customColor = White2)
                    else ComposableNormalText2(text = "NO ASSIGNADA", fontSize = 14, false, Color(0xFFFF8A80))


            }
        }

        // Altura de la barra de estado
        val heightVal = if(CurrentUser.user!!.role != "student") 90.dp
        else 70.dp

        //// ESTADO INCIDENCIA ////
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(4.dp)
                .size(
                    width = 20.dp,
                    height = heightVal
                )
                .align(Alignment.CenterEnd)
                .background(
                    color = when (incidence.status) {
                        "obert" -> openColor
                        "tancat" -> closedColor
                        else -> processColor
                    },
                    shape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 12.dp),
                )
        )
    }
    }
