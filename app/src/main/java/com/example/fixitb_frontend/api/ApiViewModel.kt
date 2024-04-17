package com.example.fixitb_frontend.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiViewModel {

    private const val BASE_URL = "http://172.30.4.168:8080"

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

}
