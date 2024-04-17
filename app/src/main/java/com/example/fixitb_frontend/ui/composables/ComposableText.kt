package com.example.fixitb_frontend.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ComposableBoldText(text: String, fontSize: Int) {
    Text(text = text, fontSize = fontSize.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun ComposableNormalText(text: String, fontSize: Int) {
    Text(text = text, fontSize = fontSize.sp, fontWeight = FontWeight.Normal)
}