package com.example.secondserving.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.secondserving.data.entities.*

@Database(entities = [
    User::class,
    Inventory::class,
    InventoryLineItem::class,
    Recipe::class,
    Unit::class,
    Ingredient::class,
    Meat::class,
    Vegetable::class,
    Fruit::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun inventoryLineItemDao(): InventoryLineItemDao
    abstract fun recipeDao(): RecipeDao
    abstract fun unitDao(): UnitDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun meatDao(): MeatDao
    abstract fun vegetableDao(): VegetableDao
    abstract fun fruitDao(): FruitDao
}


