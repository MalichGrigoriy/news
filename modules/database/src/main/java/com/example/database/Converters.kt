package com.example.database

import android.icu.text.DateFormat
import androidx.room.TypeConverter
import java.util.Date

internal class Converters {
    @TypeConverter
    public fun fromTimestamp(value: String?): Date? {
        return value?.let { DateFormat.getDateTimeInstance().parse(value) }
    }

    @TypeConverter
    public fun dateToTimestamp(date: Date?): String? {
        return date?.time?.let { DateFormat.getDateTimeInstance().format(it) }
    }
}
