package com.example.fixitb_frontend.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import com.example.fixitb_frontend.ui.theme.TertiaryColor

data class User(val mail: String)

@Composable
fun UserListItem(user: User, onConvertClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(SecondaryColor.copy(alpha = 0.4f), shape = RoundedCornerShape(5.dp))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.lionface),
                contentDescription = "User Image",
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = user.mail, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = TertiaryColor),
                    onClick = onConvertClick) {
                    Text("Convertir a técnico")
                }
            }
            Spacer(modifier = Modifier.width(30.dp))
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clickable(onClick = { /* Manejar la acción de eliminar aquí */ })
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bin),
                    contentDescription = "Delete",
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.5f // Establecer la transparencia del icono del botón de basura
                )
            }
        }
    }
}



@Composable
fun UsersScreen() {
    val usersList = listOf(
        User("roman@itb.cat"),
        User("alan@itb.cat"),
        User("albert@itb.cat")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blue1.copy(alpha = 1.0f))) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.img_logo_1),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Usuaris",
                color = Color.White,
                style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    items = usersList,
                    itemContent = {
                        UserListItem(user = it) {
                            // Aquí manejas la acción de convertir a técnico
                        }
                    })
            }
        }
    }
}

@Preview
@Composable
fun UsersScreenPreview() {
    UsersScreen()
}
