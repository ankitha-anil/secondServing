package com.example.secondserving.data

import androidx.room.TypeConverter
import java.util.Date

class LocalDateConverter {

    @TypeConverter
    fun dateToLong(date: Date?): Long? = date?.time
    @TypeConverter
    fun longToDate(dateInMillis: Long?): Date? = dateInMillis?.let { Date(dateInMillis)}
}