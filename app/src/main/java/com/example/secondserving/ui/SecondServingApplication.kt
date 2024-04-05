package com.example.secondserving.ui

import android.app.Application
import androidx.room.Room
import dagger.hilt.android.HiltAndroidApp
import com.example.secondserving.data.InventoryDatabase
import com.example.secondserving.data.RecipeDatabase
import com.example.secondserving.data.RecipeRepository
import javax.inject.Inject

@HiltAndroidApp
class SecondServingApplication: Application() {
    @Inject
    lateinit var recipeRepository: RecipeRepository

    override fun onCreate() {
        super.onCreate()
        recipeRepository.initializeDatabase()
    }
}