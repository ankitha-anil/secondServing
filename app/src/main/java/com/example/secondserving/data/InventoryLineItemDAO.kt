package com.example.secondserving.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryLineItemDAO {
    @Query("SELECT * FROM inventorylineitems_table")
    fun getAllInventoryLineItems(): Flow<List<InventoryLineItem>>

    @Query("SELECT * FROM inventorylineitems_table WHERE inventoryId = :inventoryId")
    fun getAllInventoryLineItemsByInventoryId(inventoryId: Int): Flow<List<InventoryLineItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInventoryLineItem(inventoryLineItem: InventoryLineItem)

    @Delete
    fun deleteInventoryLineItem(inventoryLineItem: InventoryLineItem)
}