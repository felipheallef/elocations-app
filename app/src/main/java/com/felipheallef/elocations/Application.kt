package com.felipheallef.elocations

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.felipheallef.elocations.data.AppDatabase

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .allowMainThreadQueries().build()

        //Stetho
//        val initializerBuilder = Stetho.newInitializerBuilder(this)
//        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//        val initializer = initializerBuilder.build()
//        Stetho.initialize(initializer)

    }

    companion object {
        var database: AppDatabase? = null
    }

}