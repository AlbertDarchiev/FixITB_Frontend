package com.example.fixitb_frontend.api

import com.example.fixitb_frontend.models.Incidence
import com.example.fixitb_frontend.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface IncidenceService {

    @GET("/incidences")
    suspend fun getIncidences(@Header("Authorization") token: String): Response<List<Incidence>>
}