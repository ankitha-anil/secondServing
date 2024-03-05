package com.example.secondserving.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(tableName = "ingredients_table")
@Parcelize
open class Ingredient(
    val name: String,
    val description: String,
    val created: Long = System.currentTimeMillis(),
    val userId: String,
    val inventoryId: Int,
    val expiry: Long,  //TODO: Change value here when you update
    @PrimaryKey(autoGenerate = true) val ingredientId: Int = 0
) : Parcelable {
    val createDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}