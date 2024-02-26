package com.example.secondserving.data.entities

import androidx.room.*
import java.util.*
@Entity(tableName = "inventories",
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE)]
)
data class Inventory(
    @PrimaryKey(autoGenerate = true) val inventoryId: Int,
    val userId: Int,
    val name: String
)