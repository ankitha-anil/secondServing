package com.example.secondserving.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryLineItemDAO {
    @Query("SELECT * FROM inventory_line_item_table")
    fun getAllInventoryLineItems(): Flow<List<InventoryLineItem>>

    @Query(
        """
    SELECT * FROM inventory_line_item_table
    INNER JOIN inventory_table ON inventory_line_item_table.inventoryID = inventory_table.id
    WHERE inventory_line_item_table.inventoryID = :inventoryID AND inventory_table.userID = :userID
"""
    )
    fun getAllInventoryLineItemsByInventoryAndUserID(
        inventoryID: Int,
        userID: String
    ): Flow<List<InventoryLineItem>>


    @Query(
        """ SELECT ingredients_table.name, inventory_line_item_table.quantity, inventory_line_item_table.expiryDate, inventory_line_item_table.id, inventoryID, inventory_line_item_table.ingredientID, inventory_line_item_table.created FROM ingredients_table
INNER JOIN inventory_line_item_table ON inventory_line_item_table.ingredientID = ingredients_table.ingredientID
WHERE inventory_line_item_table.inventoryID = :inventoryID """
    )
    fun getAllIngredientsByInventoryID(
        inventoryID: Int
    ): Flow<List<InvLineItemDisplay>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryLineItem(inventoryLineItem: InventoryLineItem)
    @Update
    suspend fun updateInventoryLineItem(inventoryLineItem: InventoryLineItem)
    @Delete
    suspend fun deleteInventoryLineItem(inventoryLineItem: InventoryLineItem)
}