package com.example.fixitb_frontend.api

import com.example.fixitb_frontend.models.Tokn
import com.example.fixitb_frontend.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @GET("/users")
    suspend fun getUsers(): Response<List<User>>

    @GET("/users")
    suspend fun getUserByEmail(@Query("email") email: String): Response<User>
    @POST("/login")
    suspend fun insertUser(@Body idTokenn: Tokn): Response<String>
}