package com.example.secondserving.data

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class IngredientTest {

    private lateinit var ingredientDao: IngredientDAO
    private lateinit var database: InventoryDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java).build()
        ingredientDao = database.ingredientDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetIngredient() = runBlocking {
        val ingredient1 = Ingredient(name = "Apple", description = "apple desc", unit = "pieces")
        ingredientDao.insertIngredient(ingredient1)

        val ingredient = ingredientDao.getAllIngredients().first()[0]
        assertEquals(ingredient.name, "Apple")

        val ingredientById = ingredientDao.getIngredientById(ingredient.ingredientID).first()[0]
        assertEquals(ingredientById.name, "Apple")
    }

    @Test
    @Throws(Exception::class)
    fun deleteIngredient() = runBlocking {
        val ingredient1 = Ingredient(name = "Apple", description = "apple desc", unit = "pieces")
        ingredientDao.insertIngredient(ingredient1)

        val ingredient = ingredientDao.getAllIngredients().first()[0]
        assertEquals(ingredient.name, "Apple")

        ingredientDao.deleteIngredient(ingredient)
        val ingredientList = ingredientDao.getAllIngredients().first()
        assertEquals(ingredientList.size, 0)
    }
}
