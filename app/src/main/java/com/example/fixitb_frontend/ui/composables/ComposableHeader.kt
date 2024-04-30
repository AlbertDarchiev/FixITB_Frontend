package com.example.fixitb_frontend.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.ui.theme.rowdiesFontFamily

@Composable
fun ComposableHeader(text: String) {
    Column {
//        Image(
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//                .width(100.dp)
//                .height(50.dp)
//            ,
//            alignment = Alignment.Center,
//            painter = painterResource(id = R.drawable.img_logo_2),
//            contentDescription = null,
//
//            contentScale = ContentScale.Crop
//        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = text,
            fontFamily = rowdiesFontFamily,
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.White,
        )
    }
}