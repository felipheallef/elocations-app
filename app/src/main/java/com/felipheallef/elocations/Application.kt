package com.felipheallef.elocations

import android.app.Application
import android.content.Context
import androidx.core.graphics.toColor
import androidx.room.Room
import com.felipheallef.elocations.data.AppDatabase
import com.felipheallef.elocations.data.model.Business
import com.google.gson.Gson

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .allowMainThreadQueries().build()

        val prefs = getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE)
        val firstRun = prefs.getBoolean("first_run", true)

        if (firstRun) {
            val business = Business("Engeselt Engenharia e Serviços",
                "A ENGESELT oferece serviços com foco no alto padrão de qualidade para garantir aos seus clientes uma parceria de sucesso.",
                "(83) 3268-5743", "Empresa",
                -7.1697895, -34.8632032)

            val inserted = database?.businessDao()?.insert(business)

            if (inserted != 0.toLong()) {
                val prefsEditor = prefs.edit()
                prefsEditor.putBoolean("first_run", false)
                prefsEditor.apply()
            }
        }

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