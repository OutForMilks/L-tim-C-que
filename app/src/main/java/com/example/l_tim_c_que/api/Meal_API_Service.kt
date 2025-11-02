package com.example.l_tim_c_que.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Meal_API_Service {

    @GET("search.php")
    fun getMealByName(
        @Query("s") name: String
    ): Call<API_Response_Models.MealListResponse>

    @GET("filter.php")
    fun getMealByIngredient(
        @Query("i") ingredient: String
    ): Call<API_Response_Models.MealListResponse>

    @GET("filter.php")
    fun getMealByArea(
        @Query("a") area: String
    ): Call<API_Response_Models.MealListResponse>

    @GET("lookup.php")
    fun getMealById(
        @Query("i") id: String
    ): Call<API_Response_Models.MealDetailResponse>

    @GET("random.php")
    fun getMealByRandom(): Call<API_Response_Models.MealDetailResponse>

}