package com.example.l_tim_c_que.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel shared between SearchFragment and HomeFragment to manage search queries and filters.
 * It allows the search state to persist when navigating between fragments.
 */
class SearchViewModel : ViewModel() {

    // Backing property for search query
    private val _searchQuery = MutableLiveData<String>()
    
    // Backing property for filter type (e.g., "name", "origin", "ingredient")
    private val _filter = MutableLiveData<String>()

    /**
     * Public read-only LiveData for the search query string.
     */
    val searchQuery: LiveData<String> get() = _searchQuery

    /**
     * Public read-only LiveData for the current filter type.
     */
    val filter: LiveData<String> get() = _filter

    /**
     * Sets or updates the current search query.
     *
     * @param query The new search query string.
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Sets or updates the current filter type.
     *
     * @param filter The new filter type (e.g., "name", "origin").
     */
    fun setFilter(filter: String) {
        _filter.value = filter
    }
}
