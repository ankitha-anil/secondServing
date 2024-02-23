package com.example.secondserving.data.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "ingredients",
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE)]
)
open class Ingredient(
    @PrimaryKey(autoGenerate = true) val ingredientId: Int,
    val name: String,
    val description: String,
    val userId: Int
)