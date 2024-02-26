package com.example.secondserving.data.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "fruits")
data class Fruit(
    @Embedded val ingredient: Ingredient
    // Other fruit-specific attributes
) : Ingredient(ingredient.ingredientId, ingredient.name, ingredient.description, ingredient.userId)
