package com.example.secondserving.data.db

import androidx.room.*
import com.example.secondserving.data.entities.*

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients")
    fun getAllIngredients(): List<Ingredient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredient: Ingredient)

    @Delete
    fun deleteIngredient(ingredient: Ingredient)

    // Add other ingredient-related queries as needed
}

@Dao
interface MeatDao {
    @Query("SELECT * FROM meats")
    fun getAllMeats(): List<Meat>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeat(meat: Meat)

    @Delete
    fun deleteMeat(meat: Meat)

    // Add other meat-related queries as needed
}

@Dao
interface VegetableDao {
    @Query("SELECT * FROM vegetables")
    fun getAllVegetables(): List<Vegetable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVegetable(vegetable: Vegetable)

    @Delete
    fun deleteVegetable(vegetable: Vegetable)

    // Add other vegetable-related queries as needed
}

@Dao
interface FruitDao {
    @Query("SELECT * FROM fruits")
    fun getAllFruits(): List<Fruit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFruit(fruit: Fruit)

    @Delete
    fun deleteFruit(fruit: Fruit)

    // Add other fruit-related queries as needed
}