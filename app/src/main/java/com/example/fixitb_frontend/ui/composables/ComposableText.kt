package com.example.fixitb_frontend.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.White1
import com.example.fixitb_frontend.ui.theme.White2
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
fun ComposableBoldText2(text: String, fontSize: Int, isCentered: Boolean = false) {
    Text(text = text, fontSize = fontSize.sp, fontWeight = FontWeight.Bold, color = White2,
        textAlign =
        if (isCentered) TextAlign.Center
        else TextAlign.Start
    )
}

@Composable
fun ComposableNormalText2(text: String, fontSize: Int, isCentered: Boolean = false, customColor: Color = White1) {
    Text(text = text, fontSize = fontSize.sp, fontWeight = FontWeight.Normal,
        color = customColor,
        textAlign = if (isCentered) TextAlign.Center else TextAlign.Start
    )
}

@Composable
fun ComposableNormalText3(text: String, fontSize: Int, isCentered: Boolean = false, customColor: Color = White1) {
    Text(text = text, fontSize = fontSize.sp, fontWeight = FontWeight.Normal,
        color = customColor,
        textAlign = if (isCentered) TextAlign.Center else TextAlign.Start
    )
}