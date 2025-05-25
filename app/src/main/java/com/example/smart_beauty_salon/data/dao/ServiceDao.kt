package com.example.smart_beauty_salon.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.smart_beauty_salon.data.model.Service
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services ORDER BY name ASC")
    fun getAllServices(): Flow<List<Service>>

    @Query("SELECT * FROM services WHERE id = :serviceId")
    fun getServiceById(serviceId: UUID): Flow<Service?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(service: Service)

    @Delete
    suspend fun delete(service: Service)

    @Update
    suspend fun update(service: Service)
}