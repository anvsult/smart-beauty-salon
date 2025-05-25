package com.example.smart_beauty_salon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "customer_preferences")
data class CustomerPreferences (
    @PrimaryKey val id: Int = 1,
    var hairType: String = "",
    var skinType: String = "",
    var preferredStylist: String = ""
)