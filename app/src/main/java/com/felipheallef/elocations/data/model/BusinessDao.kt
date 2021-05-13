package com.felipheallef.elocations.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BusinessDao {
    @Query("SELECT * FROM business")
    fun getAll(): List<Business>

    @Query("SELECT * FROM business WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<Business>

    @Query("SELECT * FROM business WHERE name LIKE :name AND description LIKE :description LIMIT 1")
    fun findByName(name: String, description: String): Business

    @Insert
    fun insertAll(vararg users: Business)

    @Delete
    fun delete(user: Business)

}