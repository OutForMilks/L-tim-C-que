package com.example.l_tim_c_que.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.l_tim_c_que.api.APIModel
import com.example.l_tim_c_que.repository.MealRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for managing bookmarked meals.
 * Handles isLoading, adding, removing, and checking status of bookmarks.
 */
class BookmarkViewModel(
    private val repository: MealRepository
) : ViewModel() {

    private val _bookmarks = MutableLiveData<List<APIModel.Meal>>()
    val bookmarks: LiveData<List<APIModel.Meal>> = _bookmarks

    private val _mealDetailsList = MutableLiveData<List<APIModel.MealDetail>>()

    val mealDetailsList: LiveData<List<APIModel.MealDetail>> = _mealDetailsList

    private val _mealDetail = MutableLiveData<APIModel.MealDetail?>()
    val mealDetail: LiveData<APIModel.MealDetail?> = _mealDetail

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _bookmarkSuccess = MutableLiveData<Boolean>()
    val bookmarkSuccess: LiveData<Boolean> = _bookmarkSuccess


    /**
     * Loads all bookmarked meals from the repository.
     * Updates [bookmarks], [isLoading], and [error] LiveData.
     */
    fun loadBookmarks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _mealDetailsList.value = repository.getBookmarkList()
                _bookmarks.value = _mealDetailsList.value.map { it.toMeal() }
            } catch (e: Exception) {
                _error.value = "Failed to load bookmarks"
            } finally {
                _isLoading.value = false
            }
        }
    }


    /**
     * Adds a meal to the bookmarks.
     *
     * @param meal The details of the meal to be bookmarked.
     */
    fun addBookmark(meal: APIModel.MealDetail) {
        repository.saveBookmark(meal) { success ->
            _bookmarkSuccess.postValue(success)
            if (success) loadBookmarks()
        }
    }


    /**
     * Removes a meal from the bookmarks.
     *
     * @param meal The details of the meal to be removed.
     */
    fun removeBookmark(meal: APIModel.MealDetail) {
        repository.removeBookmark(meal) { success ->
            _bookmarkSuccess.postValue(success)
            if (success) loadBookmarks()
        }
    }


    /**
     * Checks if a meal with the given ID is bookmarked.
     *
     * @param id The ID of the meal to check.
     * @return A [LiveData] emitting true if the meal is bookmarked, false otherwise.
     */
    fun isBookmarked(id: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        repository.isMealBookmarked(id) { isBookmarked ->
            result.postValue(isBookmarked)
        }

        return result
    }


    /**
     * Retrieves the details of a single bookmarked meal.
     *
     * @param id The ID of the meal to retrieve.
     * @return A [LiveData] emitting the meal details or null if not found/error.
     */
    fun getBookmarkDetail(id: String){

        viewModelScope.launch {
            try {
                _mealDetail.value = repository.getMealFromBookmarks(id)
            } catch (e: Exception) {
                _error.value = "Failed to load bookmark details"
            }
        }
    }

    fun getRecentDetail(id: String){

        viewModelScope.launch {
            try {
                _mealDetail.value = repository.getMealFromRecent(id)
            } catch (e: Exception) {
                _error.value = "Failed to load recently viewed details"
            }
        }
    }
}
