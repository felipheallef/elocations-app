package com.felipheallef.elocations.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.felipheallef.elocations.data.model.Business
import com.felipheallef.elocations.data.model.BusinessDao

@Database(entities = arrayOf(Business::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun businessDao(): BusinessDao
}
