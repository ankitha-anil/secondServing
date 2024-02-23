package com.example.secondserving.data.db
import androidx.room.*
import com.example.secondserving.data.entities.Inventory
import java.util.*

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventories WHERE userId = :userId")
    fun getInventoriesForUser(userId: Int): List<Inventory>

    @Insert
    fun insertInventory(inventory: Inventory)

    @Delete
    fun deleteInventory(inventory: Inventory)
}