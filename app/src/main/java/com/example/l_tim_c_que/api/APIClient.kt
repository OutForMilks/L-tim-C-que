package com.example.l_tim_c_que.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object APIClient {

    // The base URL — all requests will be built from this root.
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    // Create an HTTP client with optional logging (for debugging)
    private val client by lazy {
        val logging = HttpLoggingInterceptor().apply {
            // Logs full request and response details (URL, body, headers)
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging) // attach the logger
            .build()
    }

    // Create a single Retrofit instance for the whole app (Singleton)
    val api: APIModels by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Base URL for all API calls
            .client(client) // Use our custom client (with logging)
            .addConverterFactory(GsonConverterFactory.create()) // Converts JSON to Kotlin objects
            .build()
            .create(APIModels::class.java) // Creates the implementation of our API interface
    }
}
