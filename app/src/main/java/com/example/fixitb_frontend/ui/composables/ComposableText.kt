package com.example.fixitb_frontend.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fixitb_frontend.ui.theme.rowdiesFontFamily


@Composable
fun ComposableBoldText1(text: String, fontSize: Int, color: Color = Color.White) {
    Text(text = text, fontSize = fontSize.sp, fontWeight = FontWeight.Bold, fontFamily = rowdiesFontFamily, color = color)
}

@Composable
fun ComposableNormalText1(text: String, fontSize: Int, color: Color = Color.White) {
    Text(text = text, fontSize = fontSize.sp, fontWeight = FontWeight.Normal, fontFamily = rowdiesFontFamily, color = color)
}
@Composable
fun ComposableLightText1(text: String, fontSize: Int, color: Color = Color.White) {
    Text(text = text, fontSize = fontSize.sp, fontWeight = FontWeight.Light, fontFamily = rowdiesFontFamily, color = color)
}

@Composable
fun ComposableBoldText2(text: String, fontSize: Int) {
    Text(text = text, fontSize = fontSize.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun ComposableNormalText2(text: String, fontSize: Int) {
    Text(text = text, fontSize = fontSize.sp, fontWeight = FontWeight.Normal)
}