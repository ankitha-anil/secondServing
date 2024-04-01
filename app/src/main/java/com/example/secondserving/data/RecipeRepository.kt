package com.example.secondserving.data

import android.content.Context
import java.io.IOException

class RecipeRepository(private val context: Context) {
    fun readRecipesFromCsv(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        try {
            context.assets.open("RecipeCSV.csv").bufferedReader().useLines { lines ->
                lines.drop(1).forEach { line ->
                    val tokens = line.split(",")
                    if (tokens.size >= 5) { // Ensure there are enough data fields
                        val recipe = Recipe(
                            id = tokens[0].toInt(),
                            recipeID = tokens[1].toInt(),
                            recipeName = tokens[2],
                            created = tokens[3].toLong(),
                            recipeDescription = tokens[4]
                        )
                        recipes.add(recipe)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return recipes
    }
}
