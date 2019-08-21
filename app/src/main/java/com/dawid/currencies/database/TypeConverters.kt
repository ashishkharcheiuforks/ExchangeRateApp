package com.dawid.currencies.database

import androidx.room.TypeConverter
import com.dawid.currencies.util.convertStringtoDateTime
import org.joda.time.DateTime

class DateConverter {

    @TypeConverter
    fun toLong(date: String) : Long = convertStringtoDateTime(date).toDate().time

    @TypeConverter
    fun fromLong(date: Long) : String = DateTime(date).toString()

}