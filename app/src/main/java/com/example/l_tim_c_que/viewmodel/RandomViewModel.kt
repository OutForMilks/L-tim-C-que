package com.example.l_tim_c_que.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.l_tim_c_que.api.APIModel
import com.example.l_tim_c_que.repository.MealRepository

/**
 * ViewModel for fetching a random meal.
 *
 * @param mealRepository The repository for accessing meal data.
 */
class RandomViewModel (
    private val mealRepository: MealRepository
): ViewModel() {

    // Holds the ID of the fetched random meal.
    private val _mealId = MutableLiveData<String?>()
    /**
     * Exposes the random meal's ID to the UI.
     */
    val mealId: LiveData<String?> = _mealId

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
     * Fetches a random meal.
     * The result is posted to the [_mealId] LiveData.
     */
    fun searchMealByRandom() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = mealRepository.getMealByRandom()
                _mealId.value = response.firstOrNull()?.id
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}
