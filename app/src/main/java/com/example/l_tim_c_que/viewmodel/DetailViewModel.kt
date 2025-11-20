package com.example.l_tim_c_que.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel shared between fragments to handle the selected meal ID for details.
 * Used to pass data (the meal ID) from a list fragment (like Search or Home) to the DetailFragment.
 */
class DetailViewModel : ViewModel() {

    // Backing property
    private val _detailID = MutableLiveData<String>()

    /**
     * Public read-only LiveData that fragments can observe to get the selected meal ID.
     */
    val detailID: LiveData<String> get() = _detailID

    /**
     * Sets or updates the selected meal ID.
     *
     * @param id The unique ID of the meal to display details for.
     */
    fun setDetailID(id: String) {
        _detailID.value = id
    }
}
