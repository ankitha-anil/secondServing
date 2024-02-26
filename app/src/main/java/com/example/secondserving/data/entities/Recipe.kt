package com.example.secondserving.data.entities
import androidx.room.*
import java.util.*

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val recipeId: Int,
    val recipeName: String,
    val recipeDescription: String
)