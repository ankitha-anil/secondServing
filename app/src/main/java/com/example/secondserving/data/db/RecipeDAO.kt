package com.example.secondserving.data.db
import androidx.room.*
import com.example.secondserving.data.entities.Recipe
import java.util.*

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: Recipe)

    @Delete
    fun deleteRecipe(recipe: Recipe)
}