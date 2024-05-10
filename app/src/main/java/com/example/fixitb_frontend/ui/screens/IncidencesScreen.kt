package com.example.fixitb_frontend.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.fixitb_frontend.ui.composables.buttons.GoBackButton
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.example.fixitb_frontend.ui.theme.rowdiesFontFamily
import com.example.fixitb_frontend.viewmodel.MainViewModel
import kotlinx.coroutines.launch

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
        // Handle unsuccessful response
        Log.e("INCIDENCES", "Unsuccessful response: ${response.code()}")
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
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFa1c4fd).copy(alpha = 1f),
                    Color(0xFFc2e9fb).copy(alpha = 1f)
                )
            )
        )
        )
    {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(0.dp, 20.dp, 0.dp, 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(), ) {
                Text(
                    text = "INCIDÈNCIES",
                    fontFamily = rowdiesFontFamily,
                    fontSize = 30.sp,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

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

            LazyColumn(
                modifier = Modifier.fillMaxHeight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    items = usersListState.value,
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
//                .width(50.dp)
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

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

//// ITEM INCIDENCIA ////
@Composable
fun incidenceListItem(incidence: Incidence) {
    val openColor = Color(0xFFFF8A80)
    val processColor = Color(0xFFFFE57F)
    val closedColor = Color(0xFFCCFF90)
    val backColor = Color(0xFF0B1C38)

    val num1 = Brush.linearGradient(end= Offset(0F, 10F),colors= listOf(Color(0xFF6F96D5).copy(alpha = 0.5f), openColor))
    val num2 = Brush.linearGradient(start= Offset(0F, 10F),colors= listOf(Color(0xFF6F96D5).copy(alpha = 0.5f), closedColor))
    val num3 = Brush.linearGradient(start= Offset(0F, 0F),colors= listOf(Color(0xFF6F96D5).copy(alpha = 0.5f), processColor))



    Box(modifier = Modifier
        .padding(6.dp)
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
                    ComposableNormalText2(text = "Asignat a: "+incidence.userAssigned, fontSize = 14)


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

@Preview(
    showBackground = true,
    heightDp = 100,
    widthDp = 320,
)@Composable
fun IncidencesScreenPrev() {
    val navController = rememberNavController()
    incidenceListItem(Incidence(1, "1", "1", "1", "1", "1", "1", "", 2, "", "2332", 2323, 2))
//    IncidencesScreen(navController = navController)
}