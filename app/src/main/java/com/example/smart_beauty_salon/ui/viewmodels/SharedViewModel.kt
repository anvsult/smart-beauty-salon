package com.example.smart_beauty_salon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_beauty_salon.data.model.Service
import com.example.smart_beauty_salon.repository.AppointmentsRepository
import com.example.smart_beauty_salon.repository.ServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val servicesRepository: ServicesRepository) : ViewModel() {
    val allServices: StateFlow<List<Service>> = servicesRepository.getServices()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun getServiceById(serviceId: UUID): StateFlow<Service?> {
        return servicesRepository.getServiceById(serviceId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = null
            )
    }

}