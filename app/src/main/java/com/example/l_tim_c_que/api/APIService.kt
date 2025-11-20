package com.example.l_tim_c_que.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface defining the endpoints for the MealDB API.
 */
interface APIService {

    /**
     * Searches for meals by name.
     *
     * @param name The name of the meal to search for (e.g., "Arrabiata").
     * @return A [APIModel.MealListResponse] containing a list of matching meals.
     */
    @GET("search.php")
    suspend fun getMealByName(
        @Query("s") name: String
    ): APIModel.MealListResponse

    /**
     * Filters meals by main ingredient.
     *
     * @param ingredient The ingredient to filter by (e.g., "Chicken_Breast").
     * @return A [APIModel.MealListResponse] containing a list of meals with the specified ingredient.
     */
    @GET("filter.php")
    suspend fun getMealByIngredient(
        @Query("i") ingredient: String
    ): APIModel.MealListResponse

    /**
     * Filters meals by area (country/region).
     *
     * @param area The area to filter by (e.g., "Canadian").
     * @return A [APIModel.MealListResponse] containing a list of meals from the specified area.
     */
    @GET("filter.php")
    suspend fun getMealByArea(
        @Query("a") area: String
    ): APIModel.MealListResponse

    /**
     * Looks up a single meal by its unique ID.
     *
     * @param id The ID of the meal.
     * @return A [APIModel.MealDetailResponse] containing the details of the meal.
     */
    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ): APIModel.MealDetailResponse

    /**
     * Fetches a single random meal.
     * Note: Although the API returns a list, it usually contains only one random meal.
     *
     * @return A [APIModel.MealListResponse] containing a random meal.
     */
    @GET("random.php") // Returns 10 meals
    suspend fun getMealByRandom(): APIModel.MealListResponse

}
