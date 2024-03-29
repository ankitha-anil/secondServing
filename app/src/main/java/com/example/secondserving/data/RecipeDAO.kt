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

    @Dao
    interface RecipeDAO {
        @Query("SELECT * FROM recipe_table WHERE recipeDescription LIKE '%' || :ingredient || '%'")
        fun findRecipesWithIngredient(ingredient: String): LiveData<List<Recipe>>
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(inventory: Recipe)

}
