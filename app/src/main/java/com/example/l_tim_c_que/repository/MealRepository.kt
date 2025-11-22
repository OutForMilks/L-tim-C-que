package com.example.l_tim_c_que.repository

import com.example.l_tim_c_que.api.APIService
import com.example.l_tim_c_que.api.APIModel
import com.example.l_tim_c_que.firebase.FirebaseDB

/* TODO: Add firebase manager
         Implement Error messages
 */
/**
 * Repository class that acts as a single source of truth for meal data.
 * It abstracts the data sources (API, and potentially Firebase/Database in the future) from the rest of the app.
 *
 * @property apiService The Retrofit service used to fetch data from the network.
 */
class MealRepository(
    private val apiService: APIService
    // Add firebase manager
)
{
    /**
     * Fetches a list of meals matching the given name.
     *
     * @param name The name of the meal to search for.
     * @return A list of [APIModel.Meal] objects, or an empty list if no matches are found.
     */
    suspend fun getMealByName(name: String): List<APIModel.Meal> {
        val response = apiService.getMealByName(name)
        return response.meals ?: emptyList()
    }

    /**
     * Fetches a list of meals containing the specified ingredient.
     *
     * @param ingredient The ingredient to filter by.
     * @return A list of [APIModel.Meal] objects, or an empty list if no matches are found.
     */
    suspend fun getMealByIngredient(ingredient: String): List<APIModel.Meal> {
        val response = apiService.getMealByIngredient(ingredient)
        return response.meals ?: emptyList()
    }

    /**
     * Fetches a list of meals from a specific area.
     *
     * @param area The area to filter by (e.g., "Italian").
     * @return A list of [APIModel.Meal] objects, or an empty list if no matches are found.
     */
    suspend fun getMealByArea(area: String): List<APIModel.Meal> {
        val response = apiService.getMealByArea(area)
        return response.meals ?: emptyList()
    }

    /**
     * Fetches a random meal.
     *
     * @return A list containing a single random [APIModel.Meal], or an empty list if the request fails.
     */
    suspend fun getMealByRandom(): List<APIModel.Meal> {
        val response = apiService.getMealByRandom()
        return response.meals ?: emptyList()
    }

    /**
     * Fetches detailed information about a specific meal by its ID.
     *
     * @param id The unique ID of the meal.
     * @return A [APIModel.MealDetail] object if found, or null otherwise.
     */
    suspend fun getMealById(id: String): APIModel.MealDetail? {
        val response = apiService.getMealById(id)
        return response.meals?.firstOrNull()
    }

    /**
     * Fetches user's list of bookmarked recipes, based on [FirebaseDB.currentUser]
     *
     * @return A list of [APIModel.MealDetail] objects, or an empty list if no matches are found.
     */
    suspend fun getBookmarkList(): List<APIModel.MealDetail> {
        return FirebaseDB.getAllBookmarks()
    }

    /**
     * Fetches the user's list of recently viewed (limited to 4), based on [FirebaseDB.currentUser]
     *
     * @return A list of [APIModel.MealDetail] objects, or an empty list if no matches are found.
     */
    suspend fun getRecentlyViewedList(): List<APIModel.MealDetail> {
        return FirebaseDB.getAllRecent()
    }

    /**
     * Fetches a specific meal from the user's list of
     * bookmarks, based on [FirebaseDB.currentUser]
     *
     * @param id the id of the meal being fetched
     * @return [APIModel.MealDetail] object corresponding to the meal id, or null
     */
    suspend fun getMealFromBookmarks(id: String): APIModel.MealDetail? {
        return FirebaseDB.getSpecificBookmark(id)
    }

    /**
     * Fetches a specific meal from the user's list of
     * recently viewed, based on [FirebaseDB.currentUser]
     *
     * @param id the id of the meal being fetched
     * @return [APIModel.MealDetail] object corresponding to the meal id, or null
     */
    suspend fun getMealFromRecent(id: String): APIModel.MealDetail? {
        return FirebaseDB.getSpecificRecent(id)
    }
}
