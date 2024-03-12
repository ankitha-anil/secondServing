package com.example.secondserving.ui

import android.app.Application
import androidx.room.Room
import dagger.hilt.android.HiltAndroidApp
import com.example.secondserving.data.InventoryDatabase

@HiltAndroidApp
class SecondServingApplication: Application() {
    val data: InventoryDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            InventoryDatabase::class.java, "inventory_database"
        ).build()
    }
}