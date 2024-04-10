package com.example.secondserving.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import kotlinx.parcelize.Parcelize

@Entity(tableName = "recipe_table")
@Parcelize
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeID: Int,
    val recipeName: String,
    val created: Long = System.currentTimeMillis(),
    val recipeDescription: String,
    val recipeIngredients: String,
    val recipeSteps: String
) : Parcelable {
    val createDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}