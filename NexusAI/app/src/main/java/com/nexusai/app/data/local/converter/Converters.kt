package com.nexusai.app.data.local.converter

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Long? = value
}
