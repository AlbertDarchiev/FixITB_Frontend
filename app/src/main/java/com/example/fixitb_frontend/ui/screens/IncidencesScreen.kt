package com.example.fixitb_frontend.ui.screens

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.api.ApiViewModel
import com.example.fixitb_frontend.models.Incidence
import com.example.fixitb_frontend.models.MyNavigationRoute
import com.example.fixitb_frontend.models.User
import com.example.fixitb_frontend.ui.composables.ComposableBoldText2
import com.example.fixitb_frontend.ui.composables.ComposableHeader
import com.example.fixitb_frontend.ui.composables.ComposableNormalText2
import com.example.fixitb_frontend.ui.composables.buttons.GoBackButton
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.example.fixitb_frontend.ui.theme.rowdiesFontFamily
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

suspend fun getIncidences(): List<Incidence> {
    val response = ApiViewModel.incidenceService.getIncidences("Bearer " + CurrentUser.userToken)
    if (response.isSuccessful) {
        val incidences = response.body()
        if (incidences != null) {
            Log.d("INCIDENCES", incidences.toString())
            return incidences
        } else {
            // Handle null response body
            Log.e("INCIDENCES", "Response body is null")
            return emptyList()
        }
    } else {
        // Handle unsuccessful response
        Log.e("INCIDENCES", "Unsuccessful response: ${response.code()}")
        // You might want to throw an exception or handle this case differently
        return emptyList()
    }
}


@Composable
fun IncidencesScreen(
    navController: NavHostController,
) {
    val usersListState = remember { mutableStateOf<List<Incidence>>(emptyList()) }
    var incidenceList = listOf<Incidence>()

    // Cargar lista de incidencias desde API
    val coroutineScope = rememberCoroutineScope()
    SideEffect {
        coroutineScope.launch {
            incidenceList = getIncidences()
            usersListState.value = incidenceList
            Log.d("INCIDENCES", usersListState.value.toString())
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Blue1)
        )
    {
        GoBackButton(navController = navController, destination = MyNavigationRoute.LOGIN)

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            ComposableHeader("INCIDÈNCIES")
            LazyColumn(
                modifier = Modifier.fillMaxHeight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {

                items(
                    items = usersListState.value,
                    itemContent = {
                        incidenceListItem(incidence = it)
                    })
            }
        }
    }
}

@Composable
fun incidenceListItem(incidence: Incidence) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .background(SecondaryColor.copy(alpha = 0.4f), shape = RoundedCornerShape(5.dp))
    ){
        Column(modifier = Modifier.padding(8.dp)) {
            ComposableBoldText2(text = incidence.title, fontSize = 16)
            ComposableNormalText2(text = "Aula ${incidence.class_num} · ${incidence.device}", fontSize = 14)
            Row {
                ComposableNormalText2(text = incidence.openDate+" - ", fontSize = 14)
                if (incidence.closeDate != null && incidence.closeDate != "")
                    ComposableNormalText2(text = incidence.closeDate, fontSize = 14)
                Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                shape = CircleShape,
                                color =
                                if (incidence.status == "obert")
                                    Color.Green
                                else if (incidence.status == "tancat")
                                    Color.Red else
                                    Color.Yellow
                            ))
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 640
)@Composable
fun IncidencesScreenPrev() {
    val navController = rememberNavController()
    IncidencesScreen(navController = navController)
}