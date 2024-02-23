package com.example.secondserving.data.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "meats")
data class Meat(
    @Embedded val ingredient: Ingredient
    // Other meat-specific attributes
) : Ingredient(ingredient.ingredientId, ingredient.name, ingredient.description, ingredient.userId)
