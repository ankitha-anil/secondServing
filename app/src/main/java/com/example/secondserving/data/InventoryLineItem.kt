package com.example.secondserving.data

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import java.text.DateFormat

@Entity(tableName = "inventoryLineItems_table")
@Parcelize //TODO: check if needed
data class InventoryLineItem(
    @PrimaryKey(autoGenerate = true) val lineItemId: Int = 0,
    val inventoryId: Int,
    val ingredientId: Int,
    val expiryDate: Long, //todo: Change value when update
    val quantity: Int,
    val created: Long = System.currentTimeMillis(),
) : Parcelable {
    val createDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}