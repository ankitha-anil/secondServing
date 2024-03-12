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
        childColumns = arrayOf("inventoryId"),
        onDelete = ForeignKey.CASCADE
    )],
    tableName = "inventory_line_item_table"
)
@Parcelize
data class InventoryLineItem(
    val name: String,
    val inventoryId: Int,
    val created: Long = System.currentTimeMillis(),
    val expiryDate: Long,
    val quantity: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val createDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}

//data class InventoryAndLineItems(
//    @Embedded
//    val inventory: Inventory,
//    @Relation(
//        parentColumn = "inventoryId",
//        entityColumn = "inventoryId"
//    )
//    val inventoryLineItem: List<InventoryLineItem>
//)