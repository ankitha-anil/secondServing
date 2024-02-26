package com.example.secondserving.data.db
import androidx.room.*
import com.example.secondserving.data.entities.InventoryLineItem
import java.util.*

@Dao
interface InventoryLineItemDao {
    @Query("SELECT * FROM inventory_line_items WHERE inventoryId = :inventoryId")
    fun getLineItemsForInventory(inventoryId: Int): List<InventoryLineItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInventoryLineItem(inventoryLineItem: InventoryLineItem)

    @Delete
    fun deleteInventoryLineItem(inventoryLineItem: InventoryLineItem)
}