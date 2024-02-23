package com.example.secondserving.ui

import android.app.Application
import androidx.room.Room
import com.example.secondserving.data.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class SecondServingApplication: Application() {
    // build db
    lateinit var db: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "ssdb"
        ).build()
    }


}