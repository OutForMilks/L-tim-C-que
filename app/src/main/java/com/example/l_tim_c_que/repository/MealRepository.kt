package com.example.l_tim_c_que.repository

import com.example.l_tim_c_que.api.APIService
import com.example.l_tim_c_que.api.APIModel
import com.example.l_tim_c_que.firebase.FirebaseDB

/**
 * Repository class that acts as a single source of truth for meal data.
 * It abstracts the data sources from the rest of the app.
 *
 * @property apiService The Retrofit service used to fetch data from the network.
 */
class MealRepository(
    private val apiService: APIService
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
    * Saves a meal to the user's bookmarked list
    * @param meal the MealDetail to be saved
    * @param onComplete callback function with boolean for status
    */
    fun saveBookmark(meal: APIModel.MealDetail, onComplete: (Boolean) -> Unit) {
        FirebaseDB.saveBookmark(meal, onComplete)
    }

    /**
     * Saves a meal to the user's recently viewed list.
     * @param meal the MealDetail to be saved
     * @param onComplete callback function with boolean for status
     */
    fun updateBookmark(meal: APIModel.MealDetail, onComplete: (Boolean) -> Unit) {
        FirebaseDB.updateBookmark(meal, onComplete)
    }


    /**
     * Removes a bookmark from the user's list of bookmarks
     * @param meal the MealDetail to remove
     * @param onComplete callback function with boolean for status
     */
    fun removeBookmark(meal: APIModel.MealDetail, onComplete: (Boolean) -> Unit) {
        FirebaseDB.removeBookmark(meal, onComplete)
    }

    /**
     * Checks if meal id is in user's list of bookmarks
     * @param id meal id to check
     * @param callback contains boolean for status
     */
    fun isMealBookmarked(id: String, callback: (Boolean) -> Unit) {
        FirebaseDB.isInBookmarks(id, callback)
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
