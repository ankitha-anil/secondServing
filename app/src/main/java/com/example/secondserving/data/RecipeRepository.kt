package com.example.secondserving.data

import android.content.Context
import com.example.secondserving.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDAO,
    private val context: Context,
    @ApplicationScope private val applicationScope: CoroutineScope
) {
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
                        println("recipe added: " + recipe.recipeName)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return recipes
    }
    private suspend fun populateDatabaseFromCSV() {
        withContext(Dispatchers.IO) {
            val recipes = parseCsvFile()
            recipes.forEach { recipe ->
                recipeDao.insertRecipe(recipe)
            }
        }
    }

    private fun parseCsvFile(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        val inputStream = context.assets.open("RecipeCSV.csv")
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            reader.readLine() // Skip CSV header
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val tokens = line!!.split(",")
                if (tokens.size >= 5) {
                    val recipe = Recipe(
                        recipeID = tokens[1].toInt(),
                        recipeName = tokens[2],
                        created = tokens[3].toLong(),
                        recipeDescription = tokens[4]
                    )
                    recipes.add(recipe)
                }
            }
        }
        return recipes
    }
    fun initializeDatabase() {
        applicationScope.launch {
            if (recipeDao.countRecipes() == 0) { // Assuming you have a count method
                populateDatabaseFromCSV()
            }
        }
    }
}
