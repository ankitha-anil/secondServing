package com.example.secondserving.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDAO {
    @Query("SELECT * FROM recipes WHERE recipeDescription LIKE :ingredient")
    fun findRecipesByIngredient(ingredient: String): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(inventory: Recipe)

}
