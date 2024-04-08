package com.example.fixitb_frontend.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.SecondaryColor

@Composable
fun SignInScreen(
    navController: NavHostController,
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Magenta)
        .blur(8.dp))
    {
        Image(
            painter = painterResource(id = R.drawable.server_img_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Escala la imagen para llenar el espacio
        )
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            alignment = Alignment.Center,
            painter = painterResource(id = R.drawable.img_logo_1),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Iniciar sesió",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.White,
            style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        LoginButton(onSignInClick)

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "- o -",
            color = Color.White,
            style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp)
            .background(SecondaryColor.copy(alpha = 0.3f), shape = RoundedCornerShape(20.dp))
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally, ) {
                EmailAndPasswordFields()
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "No tens compte?",
                        color = Color.White,
                        style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    ClickableText(
                        text = AnnotatedString("Registra't"),
                        onClick = {
                            navController.navigate("register")
                        },
                    )

                }

            }
        }

    }
}

@Composable
fun EmailAndPasswordFields() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email field
            TextField(
                value = "",
                onValueChange = { /* Change email value */ },
                label = { Text("Correo electrónico") },
                modifier = Modifier.padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            // Password field
            TextField(
                value = "",
                onValueChange = { /* Change password value */ },
                label = { Text("Contraseña") },
                modifier = Modifier.padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )
        }

}

// WORKING SIGNIN BUTTON ---------------
//        Button(onClick = onSignInClick) {
//            Text(text = "Sign incsc")
//        }






@Composable
private fun LoginButton(onSignInClick: () -> Unit){
    Button(onClick = onSignInClick,
        colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor),
        modifier = Modifier
            .width(250.dp)
            .height(50.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.img_logo_google),
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .width(40.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Accedir amb Google",
                color = PrimaryColor,
                fontSize = 15.sp
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 640
)@Composable
fun SignInScreen() {
    val navController = rememberNavController() // Simular NavController
    val state = SignInState() // Simular SignInState
    SignInScreen(navController = navController, state = state, onSignInClick = {})
}