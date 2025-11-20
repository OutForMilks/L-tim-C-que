package com.example.l_tim_c_que.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DetailViewModel : ViewModel() {

    // Backing property
    private val _detailID = MutableLiveData<String>()

    // Public read-only LiveData that fragments can observe
    val detailID: LiveData<String> get() = _detailID

    // Function to set/update the id value
    fun setDetailID(id: String) {
        _detailID.value = id
    }


}