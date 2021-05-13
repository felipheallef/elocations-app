package com.felipheallef.elocations.ui.model

import android.content.ClipDescription
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.felipheallef.elocations.Application
import com.felipheallef.elocations.data.AppDatabase
import com.felipheallef.elocations.data.model.Business

class BusinessViewModel(context: Context) : ViewModel() {

    val business = MutableLiveData<Business>()
    private var db: AppDatabase?

    init {
        Application.database.also { db = it }
    }

    fun saveToDatabase(name: String, description: String, number: String, category: String, latitude: Double, longitude: Double) {
        // Do an asynchronous operation to fetch users.
        val data = Business(name, description, number, category, latitude, longitude)
        db?.businessDao()?.insertAll(data)
    }

    fun add(business: Business) {
        db?.businessDao()?.insertAll(business)
    }
}

