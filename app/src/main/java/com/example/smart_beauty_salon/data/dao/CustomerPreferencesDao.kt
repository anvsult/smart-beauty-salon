package com.example.smart_beauty_salon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smart_beauty_salon.data.model.CustomerPreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerPreferencesDao {
    @Query("SELECT * FROM customer_preferences WHERE id = 1")
    fun getPreferences(): Flow<CustomerPreferences?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePreferences(preferences: CustomerPreferences)

}