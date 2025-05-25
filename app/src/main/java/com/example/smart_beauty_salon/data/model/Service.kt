package com.example.smart_beauty_salon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "services")
data class Service (
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val price: Double,
    val duration: Int,
)