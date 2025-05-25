package com.example.smart_beauty_salon.repository

import com.example.smart_beauty_salon.data.dao.ServiceDao
import com.example.smart_beauty_salon.data.model.Service
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class ServicesRepository @Inject constructor(private val dao: ServiceDao) {
    fun getServices(): Flow<List<Service>> = dao.getAllServices()
    fun getServiceById(serviceId: UUID): Flow<Service?> = dao.getServiceById(serviceId)
    suspend fun addService(service: Service) = dao.insert(service)
    suspend fun removeService(service: Service) = dao.delete(service)
    suspend fun updateService(service: Service) = dao.update(service)
}