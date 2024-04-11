package com.example.secondserving.ui

import android.app.Application
import com.example.secondserving.data.RecipeRepository
import dagger.hilt.android.HiltAndroidApp
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