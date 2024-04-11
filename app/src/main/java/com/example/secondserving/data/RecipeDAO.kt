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
        select it.name
        from ingredients_table it
        join inventory_line_item_table ili on it.ingredientID = ili.ingredientID
        join inventory_table inv on ili.inventoryID = inv.id
        where inv.userID = :userID
        group by it.name
        order by ili.expiryDate asc
    )
    select rt.*
    from recipe_table rt
    where exists (
        select 1
        from expiringingredients
        where rt.recipeIngredients like '%'||  name || '%'
    )
""")
    fun getRecipeRecommendationsByUserID(userID: String): Flow<List<Recipe>>
    @Query("SELECT COUNT(*) FROM recipe_table")
    suspend fun countRecipes(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(inventory: Recipe)

}
