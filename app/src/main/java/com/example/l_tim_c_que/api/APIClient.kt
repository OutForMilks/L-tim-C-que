package com.example.l_tim_c_que.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Singleton object that manages the Retrofit API client configuration.
 * It provides a single instance of [APIService] for making network requests.
 */
object APIClient {

    /**
     * The base URL for the MealDB API.
     * All requests will be built relative to this URL.
     */
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    /**
     * Lazy-initialized OkHttpClient with logging capabilities.
     *
     * This client includes an [HttpLoggingInterceptor] that logs request and response body
     * details to the console, which is useful for debugging purposes.
     */
    private val client by lazy {
        val logging = HttpLoggingInterceptor().apply {
            // Logs full request and response details (URL, body, headers)
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging) // attach the logger
            .build()
    }

    /**
     * Lazy-initialized Retrofit service instance.
     *
     * This property exposes the [APIService] implementation created by Retrofit.
     * It uses the custom [client] and a [GsonConverterFactory] to parse JSON responses.
     */
    val api: APIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Base URL for all API calls
            .client(client) // Use our custom client (with logging)
            .addConverterFactory(GsonConverterFactory.create()) // Converts JSON to Kotlin objects
            .build()
            .create(APIService::class.java) // Creates the implementation of our API interface
    }
}
