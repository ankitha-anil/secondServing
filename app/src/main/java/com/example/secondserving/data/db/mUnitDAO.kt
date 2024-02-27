package com.example.secondserving.data.db

import androidx.room.*
import com.example.secondserving.data.entities.mUnit
import java.util.*

@Dao
interface mUnitDao {
    @Query("SELECT * FROM units")
    fun getAllUnits(): List<mUnit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnit(unit: mUnit)

    @Delete
    fun deleteUnit(unit: mUnit)
}