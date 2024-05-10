package com.example.fixitb_frontend.api

import com.example.fixitb_frontend.models.Tokn
import com.example.fixitb_frontend.models.User
import com.example.fixitb_frontend.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("/users")
    suspend fun getUsers(@Header("Authorization") token: String): Response<List<User>>

    @DELETE("/users/delete/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Int?, @Header("Authorization") token: String): Response<String>

    @POST("/login")
    suspend fun insertUser(@Body idTokenn: Tokn): Response<UserResponse>

    @PUT("/users/{userId}/role/{newRole}")
    suspend fun updateUserRole(
        @Path("userId") userId: Int,
        @Path("newRole") newRole: String,
        @Header("Authorization") token: String
    ): Response<Unit>

}