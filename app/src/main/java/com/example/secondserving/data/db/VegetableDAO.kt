package com.example.secondserving.data.db

import androidx.room.*
import com.example.secondserving.data.entities.Vegetable
import java.util.*

@Dao
interface VegetableDao {
    @Query("SELECT * FROM vegetables")
    fun getAllVegetables(): List<Vegetable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVegetable(vegetable: Vegetable)

    @Delete
    fun deleteVegetable(vegetable: Vegetable)
}
