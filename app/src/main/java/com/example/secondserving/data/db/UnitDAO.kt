package com.example.secondserving.data.db
import androidx.room.*
import java.util.*

@Dao
interface UnitDao {
    @Query("SELECT * FROM units")
    fun getAllUnits(): List<Unit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnit(unit: Unit)

    @Delete
    fun deleteUnit(unit: Unit)
}