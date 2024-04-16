package com.example.secondserving.data

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.Calendar

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RecipeTest {

    private lateinit var invLineItemDao: InventoryLineItemDAO
    private lateinit var inventoryDao:  InventoryDAO
    private lateinit var ingredientDao: IngredientDAO
    private lateinit var database: InventoryDatabase
    private lateinit var recipeDao: RecipeDAO

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java).build()
        invLineItemDao = database.inventoryLineItemDao()
        inventoryDao = database.inventoryDao()
        ingredientDao = database.ingredientDao()
        recipeDao = database.recipeDao()

        runBlocking {
            val inventory1 = Inventory(id = 1, name = "Inventory1", userID = "user1")
            inventoryDao.insertInventory(inventory1)

            val ingredient1 = Ingredient(ingredientID = 1, name = "Apple", description = "apple desc", unit = "pieces")
            val ingredient2 = Ingredient(ingredientID = 2, name = "Banana", description = "banana desc", unit = "bundles")
            ingredientDao.insertIngredient(ingredient1)
            ingredientDao.insertIngredient(ingredient2)
        }
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertGetCountRecipes() = runBlocking {
        val recipe1Name = "Apple Pie"
        val recipeApple = Recipe(
            recipeName = recipe1Name,
            recipeIngredients = "apple-pie",
            recipeID = 1,
            recipeDescription = "",
            recipeSteps = ""
        )

        recipeDao.insertRecipe(recipeApple)

        val numberRecipes = recipeDao.countRecipes()
        val savedRecipe = recipeDao.getAllRecipes().first()

        assertEquals(numberRecipes, 1)
        assertEquals(savedRecipe[0].recipeName, recipe1Name)
    }

    @Test
    @Throws(Exception::class)
    fun testRecipeRecommendation() = runBlocking {
        val recipeApple = Recipe(id = 1, recipeName = "Apple Pie", recipeIngredients = "apple-pie", recipeID = 1, recipeDescription = "", recipeSteps = "")
        val recipeBanana = Recipe(id = 2, recipeName = "Banana Pie", recipeIngredients = "banana-pie", recipeID = 2, recipeDescription = "", recipeSteps = "")
        recipeDao.insertRecipe(recipeApple)
        recipeDao.insertRecipe(recipeBanana)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 2)
        val expiryDate = calendar.timeInMillis

        val invLineItemApple = InventoryLineItem(
            id = 1,
            inventoryID = 1,
            ingredientID = 1,
            expiryDate = expiryDate,
            quantity = 1
        )
        val invLineItemBanana = InventoryLineItem(
            id = 2,
            inventoryID = 1,
            ingredientID = 2,
            expiryDate = expiryDate,
            quantity = 2
        )

        invLineItemDao.insertInventoryLineItem(invLineItemApple)
        invLineItemDao.insertInventoryLineItem(invLineItemBanana)

        val recipeRecommendations = recipeDao.getRecipeRecommendationsByUserID("user1").first()

        assertEquals(recipeRecommendations.size, 2)
        assertEquals(recipeRecommendations[0].recipeName, "Apple Pie")
        assertEquals(recipeRecommendations[1].recipeName, "Banana Pie")
    }
}
