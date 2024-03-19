package com.example.secondserving.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface IngredientDAO {
    @Query("SELECT * FROM ingredients_table")
    fun getAllIngredients(): Flow<List<Ingredient>>

    @Query("SELECT * FROM ingredients_table, inventory_line_item_table WHERE ingredients_table.ingredientId = :ingredientId AND inventory_line_item_table.inventoryId = :inventoryId AND inventory_line_item_table.ingredientId == ingredients_table.ingredientId")
    fun getAllIngredientsByInventory(inventoryId: Int, ingredientId: Int): Flow<List<Ingredient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)
}