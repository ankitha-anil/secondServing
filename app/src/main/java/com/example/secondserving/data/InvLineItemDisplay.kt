package com.example.secondserving.data

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
data class InvLineItemDisplay(
    val name: String,
    val expiryDate: Long,
    val quantity: Int,
    val id: Int,
    val inventoryID: Int,
    val ingredientID: Int,
    val created: Long,
) : Parcelable {

    fun toInventoryLineItem(): InventoryLineItem {
        return InventoryLineItem(
            id = this.id,
            inventoryID = this.inventoryID,
            ingredientID = this.ingredientID,
            expiryDate = this.expiryDate,
            quantity = this.quantity,
            created = this.created
        )
    }

}