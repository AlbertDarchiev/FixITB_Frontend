package com.example.fixitb_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class User(val mail: String, val userClass: String)

@Composable
fun UserItem(user: User) {

    val nameText = Text(
        text = user.mail,
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    )

    val classText = Text(
        text = user.userClass,
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    )

    val button = Button(
        onClick = { /* Convertir a técnico */ },
        colors = ButtonDefaults.buttonColors()
    ) {
        Text(text = "Convertir a técnico")
    }

    Box(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .background(color = Color.Blue) // Fondo azul oscuro
            .height(IntrinsicSize.Min) // Altura ajustada al contenido
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                nameText
                classText
            }
            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre los datos y el botón
            button
        }
    }
}


@Composable
fun UserList(users: List<User>) {
    val divider = Divider(color = Color.Black) // Cambia esto por el color que desees probar

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(users) { user ->
            UserItem(user)
            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre elementos
        }
    }
}


@Preview
@Composable
fun UserListPreview() {
    val users = listOf(
        User("roman@itb.cat", "Dami"),
        User("alan@itb.cat", "Dami"),
        User("albert@itb.cat", "Daw"),
        User("roman@itb.cat", "Dami"),
        User("alan@itb.cat", "Dami"),
        User("albert@itb.cat", "Daw"),
        User("roman@itb.cat", "Dami"),
        User("alan@itb.cat", "Dami"),
        User("albert@itb.cat", "Daw"),
        User("roman@itb.cat", "Dami"),
        User("alan@itb.cat", "Dami"),
        User("albert@itb.cat", "Daw")
    )

    UserList(users)
}