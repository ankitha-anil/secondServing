package com.example.secondserving.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(
    foreignKeys = [ForeignKey(
        entity = Inventory::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("inventoryID"),
        onDelete = ForeignKey.CASCADE
    )], tableName = "inventory_line_item_table"
)
@Parcelize
data class InventoryLineItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val inventoryID: Int,
    val ingredientID: Int,
    val expiryDate: Long,
    var quantity: Int,
    val created: Long = System.currentTimeMillis(),
) : Parcelable {
    val createDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}