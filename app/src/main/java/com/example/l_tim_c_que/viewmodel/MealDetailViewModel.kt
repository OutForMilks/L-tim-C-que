package com.example.l_tim_c_que.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.l_tim_c_que.api.APIModel
import com.example.l_tim_c_que.repository.MealRepository

/**
 * ViewModel for managing meal-related data and business logic.
 * It interacts with the [MealRepository] to fetch meal data and exposes it to the UI
 * through LiveData objects.
 *
 * @param mealRepository The repository for accessing meal data.
 */
class MealDetailViewModel (
    private val mealRepository: MealRepository
): ViewModel() {


    // Holds the details of a single meal.
    private val _mealDetail = MutableLiveData<APIModel.MealDetail?>()

    /**
     * Exposes the mealDetail detail to the UI.
     */
    val mealDetail: LiveData<APIModel.MealDetail?> = _mealDetail

    // Indicates whether a network operation is in progress.
    private val _isLoading = MutableLiveData<Boolean>()
    /**
     * Exposes the loading state to the UI.
     */
    val isLoading: LiveData<Boolean> = _isLoading

    // Holds any error message that occurs during data fetching.
    private val _errorMessage = MutableLiveData<String?>()
    /**
     * Exposes error messages to the UI.
     */
    val errorMessage: LiveData<String?> = _errorMessage

    /**
     * Searches for a meal by its ID.
     * The result is posted to the [_mealDetail] LiveData.
     * @param id The ID of the meal to search for.
     */
    fun searchMealById(id: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = mealRepository.getMealById(id)
                _mealDetail.value = response
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}