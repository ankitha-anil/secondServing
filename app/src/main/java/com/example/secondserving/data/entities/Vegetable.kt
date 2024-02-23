package com.example.secondserving.data.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "vegetables")
data class Vegetable(
    @Embedded val ingredient: Ingredient
    // Other vegetable-specific attributes
) : Ingredient(ingredient.ingredientId, ingredient.name, ingredient.description, ingredient.userId)
