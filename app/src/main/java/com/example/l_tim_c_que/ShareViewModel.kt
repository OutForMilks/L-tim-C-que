package com.example.l_tim_c_que

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * SharedViewModel is used to share data (like search queries)
 * between fragments within the same activity.
 */
class SharedViewModel : ViewModel() {

    // Backing property
    private val _searchQuery = MutableLiveData<String>()

    // Public read-only LiveData that fragments can observe
    val searchQuery: LiveData<String> get() = _searchQuery

    // Function to set/update the query value
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}