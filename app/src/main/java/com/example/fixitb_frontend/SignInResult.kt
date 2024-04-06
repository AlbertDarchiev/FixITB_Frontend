package com.example.fixitb_frontend

data class SignInResult(
    val data: UserData?,
    val errorMessages: String?,

)

data class UserData(
val userId: String,
    val username : String?,
    val email: String?,
    val picture: String?,
)
