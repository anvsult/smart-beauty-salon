package com.example.smart_beauty_salon.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.smart_beauty_salon.data.model.Appointment
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments ORDER BY appointmentDateTime DESC")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE id = :appointmentId")
    fun getAppointmentById(appointmentId: Long): Flow<Appointment?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointment: Appointment): Long

    @Delete
    suspend fun delete(appointment: Appointment)

    @Update
    suspend fun update(appointment: Appointment)
}