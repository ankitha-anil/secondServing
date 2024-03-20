package com.example.secondserving.data

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.collect
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

    private lateinit var database: InventoryDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testCreateInventoryEntity() {
        val inventory = Inventory(name = "Inventory1", userID = "user1")
        var inventoryID = 0;

        runBlocking {
            database.inventoryDao().insertInventory(inventory)
        }

        runBlocking {
            database.inventoryDao().getInventoriesForUser("user1").collect { invList ->
                assert(invList.size != 0)
                assert(invList.elementAt(0).userID.equals("user1"))
                assert(invList.elementAt(0).name.equals("Inventory1"))
            }
        }
    }

}