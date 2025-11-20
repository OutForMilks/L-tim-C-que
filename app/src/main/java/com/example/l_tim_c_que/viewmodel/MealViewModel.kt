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
class MealViewModel (
    private val mealRepository: MealRepository
): ViewModel() {

    // Holds the list of meals fetched from the API.
    private val _meals = MutableLiveData<List<APIModel.Meal>>()
    /**
     * Exposes the list of meals to the UI.
     */
    val meals: LiveData<List<APIModel.Meal>> = _meals

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
     * Searches for meals by name.
     * The result is posted to the [_meals] LiveData.
     * @param name The name of the meal to search for.
     */
    fun searchMealByName(name: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = mealRepository.getMealByName(name)
                _meals.value = response
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Searches for meals by ingredient.
     * The result is posted to the [_meals] LiveData.
     * @param ingredient The ingredient to search for.
     */
    fun searchMealByIngredient(ingredient: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = mealRepository.getMealByIngredient(ingredient)
                _meals.value = response
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Fetches a random meal.
     * The result is posted to the [_meals] LiveData.
     */
    fun searchMealByRandom() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = mealRepository.getMealByRandom()
                _meals.value = response
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Searches for meals by area (country).
     * The result is posted to the [_meals] LiveData.
     * @param country The country to search for meals from.
     */
    fun searchMealByArea(country: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = mealRepository.getMealByArea(country)
                _meals.value = response
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}