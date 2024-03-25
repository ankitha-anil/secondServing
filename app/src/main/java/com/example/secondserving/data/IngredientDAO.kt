package com.example.secondserving.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface IngredientDAO {
    @Query("SELECT * FROM ingredients_table")
    fun getAllIngredients(): Flow<List<Ingredient>>

    @Query("SELECT * FROM ingredients_table WHERE ingredientID = :ingredientID")
    fun getIngredientById(ingredientID: Int): Flow<List<Ingredient>>

    @Query("SELECT * FROM ingredients_table, inventory_line_item_table WHERE ingredients_table.ingredientID = :ingredientID AND inventory_line_item_table.inventoryID = :inventoryID AND inventory_line_item_table.ingredientID == ingredients_table.ingredientID")
    fun getAllIngredientsByInventory(inventoryID: Int, ingredientID: Int): Flow<List<Ingredient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)
}