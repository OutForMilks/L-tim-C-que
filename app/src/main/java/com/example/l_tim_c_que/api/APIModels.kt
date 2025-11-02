package com.example.l_tim_c_que.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface APIModels {

    @GET("search.php")
    fun getMealByName(
        @Query("s") name: String
    ): Call<APIService.MealListResponse>

    @GET("filter.php")
    fun getMealByIngredient(
        @Query("i") ingredient: String
    ): Call<APIService.MealListResponse>

    @GET("filter.php")
    fun getMealByArea(
        @Query("a") area: String
    ): Call<APIService.MealListResponse>

    @GET("lookup.php")
    fun getMealById(
        @Query("i") id: String
    ): Call<APIService.MealDetailResponse>

    @GET("random.php")
    fun getMealByRandom(): Call<APIService.MealDetailResponse>

}