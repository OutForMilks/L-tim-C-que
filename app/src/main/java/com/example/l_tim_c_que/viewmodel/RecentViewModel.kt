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
class RecentViewModel (
    private val repository: MealRepository
) : ViewModel() {

    private val _recent = MutableLiveData<List<APIModel.Meal>>()
    val recent : LiveData<List<APIModel.Meal>> = _recent

    private val _mealDetailsList = MutableLiveData<List<APIModel.MealDetail>>()

    val mealDetailsList: LiveData<List<APIModel.MealDetail>> = _mealDetailsList

    private val _mealDetail = MutableLiveData<APIModel.MealDetail?>()
    val mealDetail: LiveData<APIModel.MealDetail?> = _mealDetail

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    /**
     * Loads all recently viewed meals from the repository.
     * Updates [recent], [isLoading], and [error] LiveData.
     */
    fun loadRecent() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _mealDetailsList.value = repository.getRecentlyViewedList()
                _recent.value = _mealDetailsList.value!!.map { it.toMeal() }
            } catch (e: Exception) {
                _error.value = "Failed to load recents"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Retrieves the details of a single recently viewed meal.
     *
     * @param id The ID of the meal to retrieve.
     * @return A [LiveData] emitting the meal details or null if not found/error.
     */
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
