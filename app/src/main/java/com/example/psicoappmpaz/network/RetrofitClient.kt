package com.example.psicoappmpaz.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    // 10.0.2.2 apunta al localhost de tu PC desde el emulador de Android Studio
    private const val BASE_URL = "http://10.0.2.2:8081/"

    // Almacén en memoria del JWT para compartirlo entre pantallas/ViewModels
    var authToken: String? = null
    var currentUserNombre: String = ""
    var currentUserRol: String = ""

    fun getBearerToken(): String {
        val raw = authToken ?: ""
        return if (raw.startsWith("Bearer ")) raw else "Bearer $raw"
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}