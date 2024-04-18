package com.example.fixitb_frontend.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class MyNavigationActions(private val navController: NavHostController) {
    fun navigateTo(destination: MyNavigationDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
        }
    }
}

data class MyNavigationDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val iconText: String,
)

val navigationDestinations = listOf(
    MyNavigationDestination(
        route = MyNavigationRoute.INCIDENCES,
        selectedIcon = Icons.Default.Build,
        iconText = "Incidences",
    ),
    MyNavigationDestination(
        route = MyNavigationRoute.USERS,
        selectedIcon = Icons.Default.Person,
        iconText = "Users",
    ),
)

object MyNavigationRoute{
    const val INCIDENCES = "incidences"
    const val USERS = "users"
    const val LOGIN = "login"
    const val SPLASH = "splash"
}
