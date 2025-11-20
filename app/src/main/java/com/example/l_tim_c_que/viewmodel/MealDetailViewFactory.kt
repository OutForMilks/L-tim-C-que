package com.example.l_tim_c_que.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.l_tim_c_que.repository.MealRepository

/**
 * Factory for creating a [MealDetailViewModel] with a constructor that takes a [MealRepository].
 *
 * @param repository The repository for meal data.
 */
class MealDetailViewModelFactory (
    private  val repository: MealRepository
): ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given `Class`.
     *
     * @param modelClass A `Class` whose instance is requested.
     * @return A newly created ViewModel.
     * @throws IllegalArgumentException if the given `modelClass` is not a [MealDetailViewModel].
     */
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class is assignable from MealViewModel
        if(modelClass.isAssignableFrom(MealDetailViewModel::class.java)) {
            // If it is, create an instance of MealViewModel, passing the repository.
            return MealDetailViewModel(repository) as T //its safe since there is an if statement to check
        }
        // If the requested ViewModel is not MealViewModel, throw an exception.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}