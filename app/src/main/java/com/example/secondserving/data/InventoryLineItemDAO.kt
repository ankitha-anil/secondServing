package com.example.secondserving.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryLineItemDAO {
    @Query("SELECT * FROM inventory_line_item_table")
    fun getAllInventoryLineItems(): Flow<List<InventoryLineItem>>

    @Query("SELECT * FROM inventory_line_item_table,inventory_table WHERE inventoryId = :inventoryId AND userID = :userId")
    fun getAllInventoryLineItemsByInventoryAndUserId(inventoryId: Int, userId: Int): Flow<List<InventoryLineItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInventoryLineItem(inventoryLineItem: InventoryLineItem)

    @Delete
    fun deleteInventoryLineItem(inventoryLineItem: InventoryLineItem)
}