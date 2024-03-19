package com.example.secondserving.ui

import android.app.Application
import androidx.room.Room
import dagger.hilt.android.HiltAndroidApp
import com.example.secondserving.data.InventoryDatabase
import com.example.secondserving.data.RecipeDatabase

@HiltAndroidApp
class SecondServingApplication: Application() {
    val data: InventoryDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            InventoryDatabase::class.java, "inventory_database"
        ).build()
    }

    val data1: RecipeDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            RecipeDatabase::class.java, "recipe_database"
        ).build()
    }
}