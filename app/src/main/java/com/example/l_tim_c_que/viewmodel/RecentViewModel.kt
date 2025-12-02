package com.example.l_tim_c_que.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.l_tim_c_que.api.APIModel
import com.example.l_tim_c_que.repository.MealRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for managing recently viewed meals.
 * Handles loading list of recent meals and individual meal details.
 *
 * @param repository The repository for accessing meal data.
 */
class RecentViewModel (
    private val repository: MealRepository
) : ViewModel() {

    // Holds the list of recently viewed meals (simplified).
    private val _recent = MutableLiveData<List<APIModel.Meal>>()
    /**
     * Exposes the list of recently viewed meals to the UI.
     */
    val recent : LiveData<List<APIModel.Meal>> = _recent

    // Holds the list of recently viewed meal details.
    private val _mealDetailsList = MutableLiveData<List<APIModel.MealDetail>>()
    /**
     * Exposes the detailed list of recently viewed meals.
     */
    val mealDetailsList: LiveData<List<APIModel.MealDetail>> = _mealDetailsList

    // Holds the details of a specific recently viewed meal.
    private val _mealDetail = MutableLiveData<APIModel.MealDetail?>()
    /**
     * Exposes the details of a single meal to the UI.
     */
    val mealDetail: LiveData<APIModel.MealDetail?> = _mealDetail

    // Indicates whether a data loading operation is in progress.
    private val _isLoading = MutableLiveData(false)
    /**
     * Exposes the loading state to the UI.
     */
    val isLoading: LiveData<Boolean> = _isLoading

    // Holds any error message that occurs during data loading.
    private val _error = MutableLiveData<String?>()
    /**
     * Exposes error messages to the UI.
     */
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
     * Updates [_mealDetail] with the result.
     *
     * @param id The ID of the meal to retrieve.
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
