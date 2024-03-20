package com.example.secondserving.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.secondserving.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Inventory::class, InventoryLineItem::class, Ingredient::class],
    version = 5,
    exportSchema = false
)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun inventoryDao(): InventoryDAO
    abstract fun inventoryLineItemDao(): InventoryLineItemDAO
    
    abstract fun ingredientDao(): IngredientDAO

    //Dependency Injection means class that use other classes should not be responsible for creating or searching this using dagger, hilt uses dagger tool makes it easier

    class Callback @Inject constructor(
        private val database: Provider<InventoryDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() { //our own class that need instantiation and doesn't belong to third party library
        override fun onCreate(db: SupportSQLiteDatabase) { // first time when we create the database, called after build method
            super.onCreate(db)

            val inventoryDao = database.get().inventoryDao()
            val inventoryLineItemDao = database.get().inventoryLineItemDao()
            val ingredientDao = database.get().ingredientDao()
            

            applicationScope.launch {
                inventoryDao.insertInventory(Inventory("Kitchen", userID = "fSiLGeQcGDdVKHvH49jkqsGYsMz2"))
                inventoryDao.insertInventory(Inventory("Pantry", userID = "fSiLGeQcGDdVKHvH49jkqsGYsMz2"))

                inventoryDao.insertInventory(Inventory("Kitchen", userID = "ckjNy4ul2qSWfjd35O4iMoj1z2e2"))
                inventoryDao.insertInventory(Inventory("Pantry", userID = "ckjNy4ul2qSWfjd35O4iMoj1z2e2"))

                inventoryDao.insertInventory(Inventory("Kitchen", userID = "R77sUcKskwVkJSEREiCKLXFVXKd2"))
                inventoryDao.insertInventory(Inventory("Pantry", userID = "R77sUcKskwVkJSEREiCKLXFVXKd2"))

                inventoryDao.insertInventory(Inventory("Kitchen", userID = "tdhEkwujSXXbRgw3jvlE2z0qFV12"))
                inventoryDao.insertInventory(Inventory("Pantry", userID = "tdhEkwujSXXbRgw3jvlE2z0qFV12"))

                inventoryDao.insertInventory(Inventory("Kitchen", userID = "wwDMf3Q52iURZ3Fw7S1QBtRVvIs2"))
                inventoryDao.insertInventory(Inventory("Pantry", userID = "wwDMf3Q52iURZ3Fw7S1QBtRVvIs2"))

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
            }

        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }
    }
}
