package com.felipheallef.elocations.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.felipheallef.elocations.data.entity.Business
import com.felipheallef.elocations.data.entity.BusinessDao

@Database(entities = [Business::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun businessDao(): BusinessDao
}
