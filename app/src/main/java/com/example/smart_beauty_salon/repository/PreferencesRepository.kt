package com.example.smart_beauty_salon.repository

import com.example.smart_beauty_salon.data.dao.AppointmentDao
import com.example.smart_beauty_salon.data.dao.CustomerPreferencesDao
import com.example.smart_beauty_salon.data.model.CustomerPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesRepository @Inject constructor(private val dao: CustomerPreferencesDao) {
    fun getCustomerPreferences() : Flow<CustomerPreferences?> = dao.getPreferences()
    suspend fun saveCustomerPreferences(preferences: CustomerPreferences) = dao.savePreferences(preferences)
}