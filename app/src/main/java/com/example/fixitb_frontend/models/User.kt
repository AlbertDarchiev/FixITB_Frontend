package com.example.fixitb_frontend.models

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Int?,
    val role: String,
    val email: String,
    val classId: Int
)