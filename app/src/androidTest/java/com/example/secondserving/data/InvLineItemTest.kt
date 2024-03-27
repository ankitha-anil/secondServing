package com.example.secondserving.data

import androidx.room.Dao
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

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InvLineItemTest {

    private lateinit var invLineItemDao: InventoryLineItemDAO
    private lateinit var inventoryDao:  InventoryDAO
    private lateinit var ingredientDao: IngredientDAO
    private lateinit var database: InventoryDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java).build()
        invLineItemDao = database.inventoryLineItemDao()
        inventoryDao = database.inventoryDao()
        ingredientDao = database.ingredientDao()

        runBlocking {
            val inventory1 = Inventory(name = "Inventory1", userID = "user1")
            val inventory2 = Inventory(name = "Inventory2", userID = "user2")
            inventoryDao.insertInventory(inventory1)
            inventoryDao.insertInventory(inventory2)

            val ingredient1 = Ingredient(name = "Apple", description = "apple desc", unit = "pieces")
            val ingredient2 = Ingredient(name = "Ketchup", description = "ketchup desc", unit = "bottles")
            val ingredient3 = Ingredient(name = "Beef", description = "beef desc", unit = "grams")
            ingredientDao.insertIngredient(ingredient1)
            ingredientDao.insertIngredient(ingredient2)
            ingredientDao.insertIngredient(ingredient3)
        }
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetInvLineItem() = runBlocking {
        val inventory1 = inventoryDao.getInventoriesForUser("user1").first()[0]
        val inventory2 = inventoryDao.getInventoriesForUser("user2").first()[0]

        val quantity = 5
        val ingredients = ingredientDao.getAllIngredients().first()
        val invLineItem1 = InventoryLineItem(
            inventoryID = inventory1.id,
            ingredientID = ingredients[0].ingredientID,
            expiryDate = System.currentTimeMillis(),
            quantity = quantity
        )

        val invLineItem2 = InventoryLineItem(
            inventoryID = inventory1.id,
            ingredientID = ingredients[1].ingredientID,
            expiryDate = System.currentTimeMillis(),
            quantity = quantity
        )

        val invLineItem3 = InventoryLineItem(
            inventoryID = inventory2.id,
            ingredientID = ingredients[2].ingredientID,
            expiryDate = System.currentTimeMillis(),
            quantity = quantity
        )

        invLineItemDao.insertInventoryLineItem(invLineItem1)
        invLineItemDao.insertInventoryLineItem(invLineItem2)
        invLineItemDao.insertInventoryLineItem(invLineItem3)

        val savedInvLineItems = invLineItemDao.getAllInventoryLineItemsByInventoryAndUserID(
            inventory2.id,
            "user2"
        ).first()

        assertEquals(savedInvLineItems.size, 1)
        assertEquals(savedInvLineItems[0].inventoryID, inventory2.id)
        assertEquals(savedInvLineItems[0].ingredientID, ingredients[2].ingredientID)
        assertEquals(savedInvLineItems[0].quantity, quantity)
    }

    @Test
    @Throws(Exception::class)
    fun updateInvLineItem() = runBlocking {
        val inventory1 = inventoryDao.getInventoriesForUser("user1").first()[0]

        val quantity = 5
        val ingredients = ingredientDao.getAllIngredients().first()

        val invLineItem1 = InventoryLineItem(
            inventoryID = inventory1.id,
            ingredientID = ingredients[0].ingredientID,
            expiryDate = System.currentTimeMillis(),
            quantity = quantity
        )

        invLineItemDao.insertInventoryLineItem(invLineItem1)
        val savedInvLineItem = invLineItemDao.getAllInventoryLineItemsByInventoryAndUserID(
            inventory1.id,
            "user1"
        ).first()[0]

        val newQuantity = 7
        savedInvLineItem.quantity = newQuantity

        invLineItemDao.insertInventoryLineItem(savedInvLineItem)
        val updatedInvLineItems = invLineItemDao.getAllInventoryLineItemsByInventoryAndUserID(
            inventory1.id,
            "user1"
        ).first()

        assertEquals(updatedInvLineItems[0].quantity, newQuantity)
        assertEquals(updatedInvLineItems[0].inventoryID, inventory1.id)
        assertEquals(updatedInvLineItems[0].ingredientID, ingredients[0].ingredientID)
    }

    @Test
    @Throws(Exception::class)
    fun deleteInvLineItem() = runBlocking {
        val inventory1 = inventoryDao.getInventoriesForUser("user1").first()[0]

        val quantity = 5
        val ingredients = ingredientDao.getAllIngredients().first()

        val invLineItem1 = InventoryLineItem(
            inventoryID = inventory1.id,
            ingredientID = ingredients[0].ingredientID,
            expiryDate = System.currentTimeMillis(),
            quantity = quantity
        )
        val invLineItem2 = InventoryLineItem(
            inventoryID = inventory1.id,
            ingredientID = ingredients[1].ingredientID,
            expiryDate = System.currentTimeMillis(),
            quantity = quantity
        )

        invLineItemDao.insertInventoryLineItem(invLineItem1)
        invLineItemDao.insertInventoryLineItem(invLineItem2)

        val savedInvLineItem = invLineItemDao.getAllInventoryLineItemsByInventoryAndUserID(
            inventory1.id,
            "user1"
        ).first()

        invLineItemDao.deleteInventoryLineItem(savedInvLineItem[0])
        val allInvLineItems = invLineItemDao.getAllInventoryLineItems().first()

        assertEquals(allInvLineItems.size, 1)
    }
}
