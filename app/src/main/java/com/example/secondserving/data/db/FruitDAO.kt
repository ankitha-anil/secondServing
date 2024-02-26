package com.example.secondserving.data.db

import androidx.room.*
import com.example.secondserving.data.entities.Fruit
import java.util.*

@Dao
interface FruitDao {
    @Query("SELECT * FROM fruits")
    fun getAllFruits(): List<Fruit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFruit(fruit: Fruit)

    @Delete
    fun deleteFruit(fruit: Fruit)
}