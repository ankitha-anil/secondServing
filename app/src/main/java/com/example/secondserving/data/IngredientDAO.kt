package com.example.secondserving.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface IngredientDAO {
    @Query("SELECT * FROM ingredients_table WHERE userID = :userID AND inventoryID = :inventoryID")
    fun getAllIngredients(inventoryID: Int, userID :Int): Flow<List<Ingredient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredient: Ingredient)

    @Delete
    fun deleteIngredient(ingredient: Ingredient)
}