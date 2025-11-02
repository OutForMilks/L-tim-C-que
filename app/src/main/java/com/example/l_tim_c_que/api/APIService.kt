package com.example.l_tim_c_que.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface APIService {

    @GET("search.php")
    suspend fun getMealByName(
        @Query("s") name: String
    ): APIModel.MealListResponse

    @GET("filter.php")
    suspend fun getMealByIngredient(
        @Query("i") ingredient: String
    ): APIModel.MealListResponse

    @GET("filter.php")
    suspend fun getMealByArea(
        @Query("a") area: String
    ): APIModel.MealListResponse

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ): APIModel.MealDetailResponse

    @GET("random.php") // Returns 10 meals
    suspend fun getMealByRandom(): APIModel.MealListResponse

}