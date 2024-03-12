package com.example.secondserving.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface InventoryLineItemDAO {
    @Query("SELECT * FROM inventory_line_item_table WHERE inventoryId = :inventoryId")
    fun getInventoryLineItemsForUser(inventoryId: Int): List<InventoryLineItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryLineItem(inventoryLineItem: InventoryLineItem)

    @Update
    suspend fun updateInventoryLineItem(inventoryLineItem: InventoryLineItem)

    @Delete
    suspend fun deleteInventory(inventoryLineItem: InventoryLineItem)
}