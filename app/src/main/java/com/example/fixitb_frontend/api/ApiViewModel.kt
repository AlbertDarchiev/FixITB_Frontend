package com.example.fixitb_frontend.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiViewModel {
    private const val BASE_URL = "http://192.168.1.107:8080"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

}