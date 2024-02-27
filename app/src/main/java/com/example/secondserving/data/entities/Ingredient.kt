package com.example.secondserving.data.entities

import androidx.room.*

@Entity(tableName = "ingredients",
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE)]
)
open class Ingredient(
    @PrimaryKey(autoGenerate = true) open val ingredientId: Int,
    open val name: String,
    open val description: String,
    open val userId: Int
)
@Entity(tableName = "meats")
data class Meat(
    @PrimaryKey(autoGenerate = true) override val ingredientId: Int,
    override val name: String,
    override val description: String,
    override val userId: Int,
    // Other meat-specific attributes
) : Ingredient(ingredientId, name, description, userId)

@Entity(tableName = "vegetables")
data class Vegetable(
    @PrimaryKey(autoGenerate = true) override val ingredientId: Int,
    override val name: String,
    override val description: String,
    override val userId: Int,
    // Other vegetable-specific attributes
) : Ingredient(ingredientId, name, description, userId)

@Entity(tableName = "fruits")
data class Fruit(
    @PrimaryKey(autoGenerate = true) override val ingredientId: Int,
    override val name: String,
    override val description: String,
    override val userId: Int,
    // Other fruit-specific attributes
) : Ingredient(ingredientId, name, description, userId)