package com.example.psicoappmpaz.network

import android.os.Build
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    // Detecta automáticamente si corre en emulador o en celular físico
    private fun esEmulador(): Boolean {
        return Build.FINGERPRINT.contains("generic") ||
                Build.FINGERPRINT.contains("emulator") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for") ||
                Build.PRODUCT.contains("sdk")
    }

    // Emulador -> 10.0.2.2 | Celular físico por USB (con adb reverse) -> 127.0.0.1
    private val BASE_URL: String = if (esEmulador()) {
        "http://10.0.2.2:8080/"
    } else {
        "http://127.0.0.1:8080/"
    }

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