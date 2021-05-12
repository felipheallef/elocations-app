package com.felipheallef.elocations.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.felipheallef.elocations.data.model.Business

class BusinessesViewModel : ViewModel() {
    private lateinit var users: MutableLiveData<List<Business>>

    fun getUsers(): LiveData<List<Business>> {
        return users
    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}

