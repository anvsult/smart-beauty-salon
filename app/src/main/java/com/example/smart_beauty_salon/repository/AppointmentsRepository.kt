package com.example.smart_beauty_salon.repository

import com.example.smart_beauty_salon.data.dao.AppointmentDao
import com.example.smart_beauty_salon.data.model.Appointment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppointmentsRepository @Inject constructor(private val dao: AppointmentDao) {
    fun getAppointments(): Flow<List<Appointment>> = dao.getAllAppointments()
    fun getAppointmentById(appointmentId: Long): Flow<Appointment?> = dao.getAppointmentById(appointmentId)
    suspend fun addAppointment(appointment: Appointment): Long { return dao.insert(appointment)}
    suspend fun removeAppointment(appointment: Appointment) = dao.delete(appointment)
    suspend fun updateAppointment(appointment: Appointment) = dao.update(appointment)
}