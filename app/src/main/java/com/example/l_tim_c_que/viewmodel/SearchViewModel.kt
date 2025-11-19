package com.example.l_tim_c_que.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SearchViewModel : ViewModel() {

    // Backing property
    private val _searchQuery = MutableLiveData<String>()
    private val _filter = MutableLiveData<String>()

    // Public read-only LiveData that fragments can observe
    val searchQuery: LiveData<String> get() = _searchQuery

    val filter: LiveData<String> get() = _filter

    // Function to set/update the query value
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: String) {
        _filter.value = filter
    }


}