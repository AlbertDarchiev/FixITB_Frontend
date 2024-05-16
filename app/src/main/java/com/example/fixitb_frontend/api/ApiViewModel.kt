package com.example.fixitb_frontend.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

// ESTABLECER CONEXIÓN CON LA API
object ApiViewModel {

//    private const val BASE_URL = "http://172.30.3.46:8080"
//    private const val BASE_URL = "http://172.30.1.58:8080"
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
    private val _selectedIncidenceId = MutableLiveData<Int>()
    val selectedIncidenceId: LiveData<Int> = _selectedIncidenceId

    fun setSelectedIncidenceId(incidenceId: Int) {
        _selectedIncidenceId.value = incidenceId
    }
}