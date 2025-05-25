package com.example.smart_beauty_salon.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = Service::class,
            parentColumns = ["id"],
            childColumns = ["serviceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Appointment (
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val serviceId: UUID,
    val appointmentDateTime: Long,
    val customerName: String = "Local Customer",
    val customerNotes: String = "",
)