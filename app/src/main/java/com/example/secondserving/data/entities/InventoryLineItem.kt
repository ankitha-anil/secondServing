package com.example.secondserving.data.entities
import androidx.room.*
import com.example.secondserving.data.LocalDateConverter
import java.util.Date


@Entity(tableName = "inventory_line_items",
    foreignKeys = [ForeignKey(entity = Inventory::class,
        parentColumns = arrayOf("inventoryId"),
        childColumns = arrayOf("inventoryId"),
        onDelete = ForeignKey.CASCADE)]
)
@TypeConverters(value=[LocalDateConverter::class])
data class InventoryLineItem(
    @PrimaryKey(autoGenerate = true) val inventoryLineItemId: Int,
    val inventoryId: Int,
    val quantity: Int,
    val expiryDate: Date
)