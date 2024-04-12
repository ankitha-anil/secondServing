package com.example.secondserving.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.secondserving.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Inventory::class, InventoryLineItem::class, Ingredient::class, Recipe::class],
    version = 6,
    exportSchema = false
)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun inventoryDao(): InventoryDAO
    abstract fun inventoryLineItemDao(): InventoryLineItemDAO
    
    abstract fun ingredientDao(): IngredientDAO

    abstract fun recipeDao(): RecipeDAO



    //Dependency Injection means class that use other classes should not be responsible for creating or searching this using dagger, hilt uses dagger tool makes it easier

    class Callback @Inject constructor(
        private val database: Provider<InventoryDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope,
        private val context: Context // This is correct now, no need to get context from database
    ) : RoomDatabase.Callback() { //our own class that need instantiation and doesn't belong to third party library
        override fun onCreate(db: SupportSQLiteDatabase) { // first time when we create the database, called after build method
            super.onCreate(db)

            val inventoryDao = database.get().inventoryDao()
            val inventoryLineItemDao = database.get().inventoryLineItemDao()
            val ingredientDao = database.get().ingredientDao()
            val recipeDao = database.get().recipeDao()

            

            applicationScope.launch {
                inventoryDao.insertInventory(Inventory("Kitchen", userID = "fSiLGeQcGDdVKHvH49jkqsGYsMz2"))
                inventoryDao.insertInventory(Inventory("Pantry", userID = "fSiLGeQcGDdVKHvH49jkqsGYsMz2"))

                inventoryDao.insertInventory(Inventory("Kitchen", userID = "R77sUcKskwVkJSEREiCKLXFVXKd2"))
                inventoryDao.insertInventory(Inventory("Pantry", userID = "R77sUcKskwVkJSEREiCKLXFVXKd2"))

                inventoryDao.insertInventory(Inventory("Kitchen", userID = "tdhEkwujSXXbRgw3jvlE2z0qFV12"))
                inventoryDao.insertInventory(Inventory("Pantry", userID = "tdhEkwujSXXbRgw3jvlE2z0qFV12"))

                inventoryDao.insertInventory(Inventory("Kitchen", userID = "wwDMf3Q52iURZ3Fw7S1QBtRVvIs2"))
                inventoryDao.insertInventory(Inventory("Pantry", userID = "wwDMf3Q52iURZ3Fw7S1QBtRVvIs2"))

                // for chin's testing
                inventoryDao.insertInventory(Inventory("Freech", userID = "ckjNy4ul2qSWfjd35O4iMoj1z2e2"))
                inventoryDao.insertInventory(Inventory("Pentree", userID = "ckjNy4ul2qSWfjd35O4iMoj1z2e2"))

                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 9,
                        ingredientID = 1,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 1,
                    )
                )
                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 9,
                        ingredientID = 2,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 1,
                    )
                )
                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 9,
                        ingredientID = 3,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 1,
                    )
                )
                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 10,
                        ingredientID = 1,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 1,
                    )
                )
                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 10,
                        ingredientID = 2,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 1,
                    )
                )
                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 10,
                        ingredientID = 3,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 1,
                    )
                )




                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 1,
                        ingredientID = 1,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 1,
                    )
                )
                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 2,
                        ingredientID = 1,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 3,
                    )
                )
                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 1,
                        ingredientID = 2,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 5,
                    )
                )
                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 2,
                        ingredientID = 2,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 7,
                    )
                )
                inventoryLineItemDao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryID = 1,
                        ingredientID = 3,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 9,
                    )
                )

                ingredientDao.insertIngredient(
                    Ingredient(
                        name = "Apple",
                        description = "Fruit",
                        unit = "Pieces"
                    )
                )
                ingredientDao.insertIngredient(
                    Ingredient(
                        name = "Carrot",
                        description = "Veggie",
                        unit = "Pieces"
                    )
                )
                ingredientDao.insertIngredient(
                    Ingredient(
                        name = "Orange",
                        description = "Fruit",
                        unit = "Pieces"
                    )
                )
                ingredientDao.insertIngredient(
                    Ingredient(
                        name = "Rice",
                        description = "Grain",
                        unit = "Kilograms"
                    )
                )
                ingredientDao.insertIngredient(
                    Ingredient(
                        name = "Wheat",
                        description = "Grain",
                        unit = "Kilogram"
                    )
                )
                ingredientDao.insertIngredient(
                    Ingredient(
                        name = "Strawberry",
                        description = "Fruit",
                        unit = "Pieces"
                    )
                )
                ingredientDao.insertIngredient(
                    Ingredient(
                        name = "Banana",
                        description = "Fruit",
                        unit = "Pieces"
                    )
                )
                ingredientDao.insertIngredient(
                    Ingredient(
                        name = "Blueberry",
                        description = "Fruit",
                        unit = "Packet"
                    )
                )
                ingredientDao.insertIngredient(
                    Ingredient(
                        name = "Tomato",
                        description = "Vegetable",
                        unit = "Kilograms"
                    )
                )
                ingredientDao.insertIngredient(
                    Ingredient(
                        name = "Lemon",
                        description = "Fruit",
                        unit = "Pieces"
                    )
                )

                populateDatabaseFromCSV()
            }

        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }

        private fun populateDatabaseFromCSV() {
            val dao = database.get().recipeDao()
            val inputStream = context.assets.open("RecipeCSV.csv") // Use the context directly
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))

            applicationScope.launch(Dispatchers.IO) {
                bufferedReader.use { reader ->
                    reader.readLine() // Skip CSV header
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val tokens = line!!.split(",")
                        if (tokens.size >= 7) {
                            val recipe = Recipe(
                                id = tokens[0].toInt(),
                                recipeID = tokens[1].toInt(),
                                recipeName = tokens[2],
                                created = tokens[3].toLong(),
                                recipeDescription = tokens[4],
                                recipeIngredients = tokens[5],
                                recipeSteps = tokens [6]
                            )
                            dao.insertRecipe(recipe)
                        }
                    }
                }
            }
        }
    }
}
