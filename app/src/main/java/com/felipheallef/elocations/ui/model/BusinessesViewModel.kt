package com.felipheallef.elocations.ui.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.felipheallef.elocations.Application
import com.felipheallef.elocations.data.AppDatabase
import com.felipheallef.elocations.data.BusinessFormState
import com.felipheallef.elocations.data.entity.Business

class BusinessesViewModel(context: Context) : ViewModel() {

    val business = MutableLiveData<List<Business>>()
    val formState = MutableLiveData<BusinessFormState>()
    private var db: AppDatabase?

    init {
        Application.database.also { db = it }
        loadBusinesses()
    }

    fun loadBusinesses() {
        // Do an asynchronous operation to fetch users.
        business.value = db?.businessDao()?.getAll()
    }

    fun add(business: Business): Long? {
        return db?.businessDao()?.insert(business)
    }

}

