package com.example.fixitb_frontend.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

// ESTABLECER CONEXIÓN CON LA API
object ApiViewModel {

//    private const val BASE_URL = "http://172.30.2.108:8080"
    private const val BASE_URL = "http://192.168.1.21:8080"

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
    val incidenceService: IncidenceService by lazy {
        retrofit.create(IncidenceService::class.java)
    }

}
