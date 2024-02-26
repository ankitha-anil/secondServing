package com.example.secondserving.data.db

import androidx.room.*
import com.example.secondserving.data.entities.User
import java.util.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)
}