package com.example.secondserving.data.entities
import androidx.room.*
import java.util.*

@Entity(tableName = "inventory_line_items",
    foreignKeys = [ForeignKey(entity = Inventory::class,
        parentColumns = arrayOf("inventoryId"),
        childColumns = arrayOf("inventoryId"),
        onDelete = ForeignKey.CASCADE)]
)
data class InventoryLineItem(
    @PrimaryKey(autoGenerate = true) val inventoryLineItemId: Int,
    val inventoryId: Int,
    val quantity: Int,
    val expiryDate: Date
)