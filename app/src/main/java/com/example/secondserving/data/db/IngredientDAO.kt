package com.example.secondserving.data.db

import androidx.room.*
import com.example.secondserving.data.entities.Ingredient
import java.util.*

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients")
    fun getAllIngredients(): List<Ingredient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredient: Ingredient)

    @Delete
    fun deleteIngredient(ingredient: Ingredient)
}