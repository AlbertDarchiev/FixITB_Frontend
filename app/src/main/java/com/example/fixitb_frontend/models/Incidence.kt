package com.example.fixitb_frontend.models

data class Incidence(
    val id: Int,
    val device: String,
    val image : String,
    val title: String,
    val description: String,
    val openDate: String,
    val closeDate: String?,
    val status: String,
    val classNum: Int,
    val userAssigned: String?,
    val codeMain: String,
    val codeMovistar: Int
)