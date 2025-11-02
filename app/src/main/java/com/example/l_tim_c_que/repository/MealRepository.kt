package com.example.l_tim_c_que.repository

import com.example.l_tim_c_que.api.APIService
import com.example.l_tim_c_que.api.APIModel

/* TODO: Add firebase manager
         Implement Error messages
 */
class MealRepository(
    private val apiService: APIService
    // Add firebase manager
)
{
    suspend fun getMealByName(name: String): List<APIModel.Meal> {
        val response = apiService.getMealByName(name)
        return response.meals ?: emptyList()
    }

    suspend fun getMealByIngredient(ingredient: String): List<APIModel.Meal> {
        val response = apiService.getMealByIngredient(ingredient)
        return response.meals ?: emptyList()
    }

    suspend fun getMealByArea(area: String): List<APIModel.Meal> {
        val response = apiService.getMealByArea(area)
        return response.meals ?: emptyList()
    }

    suspend fun getMealByRandom(): List<APIModel.Meal> {
        val response = apiService.getMealByRandom()
        return response.meals ?: emptyList()
    }

    suspend fun getMealById(id: String): APIModel.MealDetail? {
        val response = apiService.getMealById(id)
        return response.meals?.firstOrNull()
    }





}