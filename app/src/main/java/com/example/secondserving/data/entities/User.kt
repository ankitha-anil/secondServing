package com.example.secondserving.data.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    val email: String,
    val username: String,
    val password: String
)
