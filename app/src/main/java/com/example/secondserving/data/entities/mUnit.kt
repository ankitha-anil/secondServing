package com.example.secondserving.data.entities

import androidx.room.*

@Entity(tableName = "units")
data class mUnit(
    @PrimaryKey(autoGenerate = true) val unitId: Int,
    val unitName: String
)
