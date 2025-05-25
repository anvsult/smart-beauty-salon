package com.example.smart_beauty_salon.data.converters

import androidx.room.TypeConverter
import java.util.UUID

class UUIDConverter {
    @TypeConverter
    fun fromUUID(id: UUID): String = id.toString()

    @TypeConverter
    fun toUUID(id: String): UUID = UUID.fromString(id)
}