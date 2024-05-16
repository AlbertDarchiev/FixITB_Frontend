package com.example.fixitb_frontend.api

import com.example.fixitb_frontend.models.Incidence
import com.example.fixitb_frontend.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IncidenceService {

    @GET("/incidences")
    suspend fun getIncidences(@Header("Authorization") token: String): Response<List<Incidence>>

    @GET("/incidences/{userId}")
    suspend fun getIncidencesForUser(@Path("userId") userId: Int?, @Header("Authorization") token: String): Response<List<Incidence>>

    @POST("/incidences")
    suspend fun insertIncidence(@Header("Authorization") token: String, @Body incidence: Incidence): Response<Incidence>

    @PUT("/incidences/{incidenceId}")
    suspend fun updateIncidenceById(@Header("Authorization") token: String, @Path("incidenceId") incidenceId: Int, @Body incidence: Incidence): Response<Unit>

    @GET("/incidences/incidence/{incidenceId}")
    suspend fun getIncidenceById(
        @Header("Authorization") token: String,
        @Path("incidenceId") incidenceId: Int
    ): Response<Incidence>

    @GET("/incidences/tecnic/{email}")
    suspend fun getIncidencesForUserAssigned(@Path("email") email: String, @Header("Authorization") token: String): Response<List<Incidence>>


    @DELETE("/incidences/delete/{incidenceId}")
    suspend fun deleteIncidenceById(@Header("Authorization") token: String, @Path("incidenceId") incidenceId: Int): Response<Unit>
}