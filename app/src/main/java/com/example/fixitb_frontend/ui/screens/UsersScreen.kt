//package com.example.fixitb_frontend.ui.screens
//
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.SideEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.example.fixitb_frontend.api.ApiViewModel
//import com.example.fixitb_frontend.models.User
//import com.example.fixitb_frontend.ui.theme.Blue1
//import com.example.fixitb_frontend.ui.theme.SecondaryColor
//import kotlinx.coroutines.launch
//import com.example.fixitb_frontend.R
//
//
//suspend fun <User> getUsers(): List<com.example.fixitb_frontend.models.User> {
//    val response = ApiViewModel.userService.getUsers("Bearer " + CurrentUser.userToken)
//    if (response.isSuccessful) {
//        val users = response.body()
//        if (users != null) {
//            Log.d("USERS", users.toString())
//            return users
//        } else {
//            // Handle null response body
//            Log.e("USERS", "Response body is null")
//            return emptyList()
//        }
//    } else {
//        // Handle unsuccessful response
//        Log.e("USERS", "Unsuccessful response: ${response.code()}")
//        // You might want to throw an exception or handle this case differently
//        return emptyList()
//    }
//}
//
//@Composable
//fun UsersScreen(
//    navController: NavHostController,
//    usersViewModel:
//) {
//    val usersListState = remember { mutableStateOf<List<User>>(emptyList()) }
//    var userList = listOf<User>()
//
//    // Filtro de rol
//    val selectedRole = remember { mutableStateOf<String?>(null) }
//
//    // Cargar lista de usuarios desde la API
//    val coroutineScope = rememberCoroutineScope()
//    SideEffect {
//        coroutineScope.launch {
//            userList = getUsers<Any?>()
//            usersListState.value = userList
//        }
//    }
//
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(Blue1)
//    ) {
//        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            // Diseño para título y logo a la derecha
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    modifier = Modifier.padding(top = 15.dp),
//                    text = "USUARIOS",
//                    color = Color.White,
//                    style = TextStyle(fontSize = 35.sp)
//                )
//                // Imagen de la empresa
//                Image(
//                    modifier = Modifier.size(75.dp),
//                    painter = painterResource(id = R.drawable.img_logo_1),
//                    contentDescription = null
//                )
//            }
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Selector de roles
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                RoleFilterButton(
//                    text = "Estudiante",
//                    onClick = { selectedRole.value = "student" },
//                    selected = selectedRole.value == "student"
//                )
//                RoleFilterButton(
//                    text = "Técnico",
//                    onClick = { selectedRole.value = "tecnic" },
//                    selected = selectedRole.value == "tecnic"
//                )
//                RoleFilterButton(
//                    text = "Administrador",
//                    onClick = { selectedRole.value = "admin" },
//                    selected = selectedRole.value == "admin"
//                )
//            }
//
//            LazyColumn(
//                modifier = Modifier.fillMaxHeight(1f),
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
//            ) {
//                val filteredUsers = usersListState.value.filter {
//                    selectedRole.value == null || it.role == selectedRole.value
//                }
//                items(items = filteredUsers) { user ->
//                    userListItem(user = user) {
//                        // Hacer clic en el botón de la papelera
//                        coroutineScope.launch {
//                            try {
//                                CurrentUser.userToken?.let { token ->
//                                    val response = usersViewModel.deleteUser(user.id, token)
//                                    if (response.isSuccessful) {
//                                        // Eliminación exitosa, actualizar la lista de usuarios
//                                        userList = userList.filterNot { it.id == user.id }
//                                        usersListState.value = userList
//                                    } else {
//                                        // Manejar la eliminación no exitosa
//                                        Log.e("DeleteUser", "Unsuccessful response: ${response.code()}")
//                                    }
//                                }
//                            } catch (e: Exception) {
//                                // Manejar cualquier excepción que ocurra durante la eliminación
//                                Log.e("DeleteUser", "Error deleting user: ${e.message}")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun RoleFilterButton(
//    text: String,
//    onClick: () -> Unit,
//    selected: Boolean
//) {
//    Box(
//        modifier = Modifier
//            .clickable(onClick = onClick)
//            .background(if (selected) Color.Blue else Color.Transparent)
//            .padding(8.dp)
//            .clip(RoundedCornerShape(4.dp))
//    ) {
//        Text(
//            text = text,
//            color = Color.White,
//            style = TextStyle(fontSize = 16.sp),
//            modifier = Modifier.padding(8.dp)
//        )
//    }
//}
//
//@Composable
//fun userListItem(user: User, onDeleteClicked: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .background(SecondaryColor.copy(alpha = 0.4f), shape = RoundedCornerShape(5.dp))
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(modifier = Modifier.weight(1f).padding(8.dp)) {
//                Text(
//                    text = user.email,
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )
//                )
//                // Agrega cualquier otra información relevante del usuario aquí
//            }
//            Box(
//                modifier = Modifier
//                    .size(55.dp)
//                    .padding(8.dp)
//                    .clickable(onClick = {
//                        Log.d("DeleteUser", "Deleting user: ${user.id}, ${user.email}")
//                        onDeleteClicked()
//                    }) // Hacer clic en el botón de la papelera
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.bin),
//                    contentDescription = "Delete",
//                    modifier = Modifier.fillMaxSize(),
//                    alpha = 0.5f
//                )
//            }
//        }
//    }
//}
//
//
//
//
//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    heightDp = 640
//)
//@Composable
//fun UsersScreenPrev() {
//    val navController = rememberNavController()
//    val usersViewModel = remember { UsersViewModel() } // Instancia de UsersViewModel
//
//    UsersScreen(navController = navController, usersViewModel = usersViewModel)
//}
