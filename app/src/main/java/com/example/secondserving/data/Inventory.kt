package com.example.secondserving.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import kotlinx.parcelize.Parcelize

@Entity(tableName = "inventory_table")
@Parcelize
data class Inventory(
    val name: String,
    val created: Long = System.currentTimeMillis(),
    val userID: String,
    @PrimaryKey(autoGenerate = true) val inventoryId: Int = 0
): Parcelable {
    val createDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}