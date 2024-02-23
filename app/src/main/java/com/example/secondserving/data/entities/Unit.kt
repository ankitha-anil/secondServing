package com.example.secondserving.data.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "units")
data class Unit(
    @PrimaryKey(autoGenerate = true) val unitId: Int,
    val unitName: String
)