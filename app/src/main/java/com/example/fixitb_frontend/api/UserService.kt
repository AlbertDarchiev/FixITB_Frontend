package com.example.fixitb_frontend.api

import com.example.fixitb_frontend.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @GET("/users")
    suspend fun getUsers(): List<User>
    @POST("/users")
    suspend fun insertUser(email: String, classId: Int, role: String): Response<User>
}