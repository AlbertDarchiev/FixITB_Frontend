package com.example.fixitb_frontend.api

import com.example.fixitb_frontend.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @GET("/users")
    suspend fun getUsers(): List<User>
    @POST("/users")
    suspend fun insertUser(@Body userData: User): Response<User>
}