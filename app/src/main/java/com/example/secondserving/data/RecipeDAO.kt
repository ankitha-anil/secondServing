package com.example.secondserving.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO {
    @Query("SELECT * FROM recipe_table")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe_table WHERE recipeDescription LIKE '%' || :ingredient || '%'")
    fun findRecipesWithIngredient(ingredient: String): Flow<List<Recipe>>

    @Query("""
        with expiringingredients as (
        select distinct ingredients_table.name
        from (
            select inventory_line_item_table.*
            from inventory_line_item_table inner join inventory_table
                on inventory_line_item_table.inventoryID = inventory_table.id
            where inventory_table.userID = :userID
            ) userinvlineitems inner join ingredients_table
            on userinvlineitems.ingredientID = ingredients_table.ingredientID
        order by userinvlineitems.expiryDate asc
        limit 3
        )
          SELECT r.*
            FROM recipe_table r
            JOIN expiringingredients exi
                ON r.recipeDescription
                LIKE '%' || exi.name || '%';
    """)
    fun getRecipeRecommendationsByUserID(userID: String): Flow<List<Recipe>>

    @Query("SELECT COUNT(*) FROM recipe_table")
    suspend fun countRecipes(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(inventory: Recipe)

}
