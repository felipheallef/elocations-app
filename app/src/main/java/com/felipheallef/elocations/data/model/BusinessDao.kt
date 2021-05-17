package com.felipheallef.elocations.data.model

import androidx.room.*

@Dao
interface BusinessDao {
    @Query("SELECT * FROM business")
    fun getAll(): List<Business>

    @Query("SELECT * FROM business WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<Business>

    @Query("SELECT * FROM business WHERE name LIKE :name AND description LIKE :description LIMIT 1")
    fun findByName(name: String, description: String): Business

    @Insert
    fun insert(business: Business): Long

    @Insert
    fun insertAll(vararg businesses: Business)

    @Delete
    fun delete(business: Business): Int

    @Update
    fun update(business: Business)

}