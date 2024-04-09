package com.example.fixitb_frontend.models


data class User(
    val id: Int,
    val role: String,
    val email: String,
    val classId: Int
)