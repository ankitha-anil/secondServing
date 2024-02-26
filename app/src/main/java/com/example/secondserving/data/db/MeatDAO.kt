package com.example.secondserving.data.db

import androidx.room.*
import com.example.secondserving.data.entities.Meat
import java.util.*

@Dao
interface MeatDao {
    @Query("SELECT * FROM meats")
    fun getAllMeats(): List<Meat>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeat(meat: Meat)

    @Delete
    fun deleteMeat(meat: Meat)
}