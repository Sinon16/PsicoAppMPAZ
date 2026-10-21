package com.example.psicoappmpaz.data.remote

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/sincronizar")
    suspend fun sincronizarConServidorCentral(@Body datos: SyncModel): Response<Void>

    object RetrofitClient {
        private const val BASE_URL = "http://10.0.2.2:3000/" // IP local estándar para emulador Android

        val api: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}