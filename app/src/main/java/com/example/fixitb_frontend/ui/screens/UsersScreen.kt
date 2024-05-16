package com.example.fixitb_frontend.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fixitb_frontend.api.ApiViewModel
import com.example.fixitb_frontend.models.User
import com.example.fixitb_frontend.ui.theme.Blue1
import com.example.fixitb_frontend.ui.theme.SecondaryColor
import kotlinx.coroutines.launch
import com.example.fixitb_frontend.R
import com.example.fixitb_frontend.ui.composables.ComposableHeader
import com.example.fixitb_frontend.ui.theme.BackColor1
import com.example.fixitb_frontend.ui.theme.BackColor2
import com.example.fixitb_frontend.ui.theme.PrimaryColor
import com.example.fixitb_frontend.ui.theme.TertiaryColor


suspend fun <User> getUsers(): List<com.example.fixitb_frontend.models.User> {
    val response = ApiViewModel.userService.getUsers("Bearer " + CurrentUser.userToken)
    if (response.isSuccessful) {
        val users = response.body()
        if (users != null) {
            Log.d("USERS", users.toString())
            return users
        } else {
            // Handle null response body
            Log.e("USERS", "Response body is null")
            return emptyList()
        }
    } else {
        // Handle unsuccessful response
        Log.e("USERS", "Unsuccessful response: ${response.code()}")
        // You might want to throw an exception or handle this case differently
        return emptyList()
    }
}

@Composable
fun UsersScreen(
    navController: NavHostController) {
    val usersListState = remember { mutableStateOf<List<User>>(emptyList()) }
    var userList = listOf<User>()

    // Filtro de rol
    val selectedRole = remember { mutableStateOf<String?>(null) }

    // Cargar lista de usuarios desde la API
    val coroutineScope = rememberCoroutineScope()
    SideEffect {
        coroutineScope.launch {
            userList = getUsers<Any?>()
            usersListState.value = userList
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    BackColor1,
                    BackColor2
                )
            )
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(30.dp))
            ComposableHeader(text = "USUARIS", width = 0.8f, size = 30)
            Spacer(modifier = Modifier.height(20.dp))

            // Selector de roles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RoleFilterButton(
                    text = "Alumnes",
                    onClick = { selectedRole.value = "student" },
                    selected = selectedRole.value == "student"
                )
                RoleFilterButton(
                    text = "Tècnics",
                    onClick = { selectedRole.value = "tecnic" },
                    selected = selectedRole.value == "tecnic"
                )
                RoleFilterButton(
                    text = "Administradors",
                    onClick = { selectedRole.value = "admin" },
                    selected = selectedRole.value == "admin"
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxHeight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val filteredUsers = usersListState.value.filter {
                    selectedRole.value == null || it.role == selectedRole.value
                }
                items(items = filteredUsers) { user ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        userListItem(
                            user = user,
                            onDeleteClicked = {
                                coroutineScope.launch {
                                    try {
                                        CurrentUser.userToken?.let { token ->
                                            val response = ApiViewModel.userService.deleteUser(user.id, "Bearer "+token)
                                            if (response.isSuccessful) {
                                                userList = userList.filterNot { it.id == user.id }
                                                usersListState.value = userList
                                            } else Log.e("DeleteUser", "Unsuccessful response: ${response.code()}")
                                        }
                                    } catch (e: Exception) {
                                        // Manejar cualquier excepción que ocurra durante la eliminación
                                        Log.e("DeleteUser", "Error deleting user: ${e.message}")
                                    }
                                }
                            },
                            onConvertClicked = {
                                coroutineScope.launch {
                                    try {
                                        CurrentUser.userToken?.let { token ->
                                            val response = user.id?.let {
                                                ApiViewModel.userService.updateUserRole(it, "tecnic", "Bearer "+token) }
                                            if (response?.isSuccessful == true) {
                                                // Actualización exitosa.
                                            } else {
                                                // Manejar la actualización no exitosa
                                                Log.e("UpdateUserRole", "Unsuccessful response: ${response?.code()}")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        // Manejar  excepción
                                        Log.e("UpdateUserRole", "Error updating user role: ${e.message}")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RoleFilterButton(
    text: String,
    onClick: () -> Unit,
    selected: Boolean
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(if (selected) PrimaryColor else SecondaryColor, shape = RoundedCornerShape(10.dp))
            .padding(8.dp)
        ) {
        Text(
            text = text,
            color = Color.White,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun userListItem(
    user: User,
    onDeleteClicked: () -> Unit,
    onConvertClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(PrimaryColor.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(8.dp)) {
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                if (user.role == "student") {
                    Button(
                        onClick = onConvertClicked,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor, contentColor = TertiaryColor.copy(alpha = 0.8f)),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Pasar a Tècnic")
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
                    .clickable(onClick = onDeleteClicked)
            ) {
                Image(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.8f
                )
            }
        }
    }
}
