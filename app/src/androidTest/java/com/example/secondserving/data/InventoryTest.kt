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

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InventoryTest {

    private lateinit var inventoryDao: InventoryDAO
    private lateinit var database: InventoryDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java).build()
        inventoryDao = database.inventoryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetInventory() = runBlocking {
        val inventory = Inventory(name = "Inventory1", userID = "user1")
        inventoryDao.insertInventory(inventory)
        val allInventories = inventoryDao.getInventoriesForUser("user1").first()
        assertEquals(allInventories[0].name, (inventory.name))
    }
}