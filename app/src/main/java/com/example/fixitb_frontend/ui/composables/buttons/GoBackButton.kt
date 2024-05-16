package com.example.fixitb_frontend.ui.composables.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.models.MyNavigationRoute
import com.example.fixitb_frontend.ui.theme.SecondaryColor

@Composable
fun GoBackButton(navController: NavController, destination: String){
    IconButton(onClick = {navController.navigate(destination)},
        modifier = Modifier
            .padding(5.dp)
            .background(SecondaryColor, shape = RoundedCornerShape(12.dp))
    ) {
        Image(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "EXIT",
            Modifier.size(24.dp)
        )
}
}
