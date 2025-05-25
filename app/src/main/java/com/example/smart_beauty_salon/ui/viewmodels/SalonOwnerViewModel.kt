package com.example.smart_beauty_salon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_beauty_salon.data.model.Appointment
import com.example.smart_beauty_salon.data.model.Service
import com.example.smart_beauty_salon.repository.AppointmentsRepository
import com.example.smart_beauty_salon.repository.ServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalonOwnerViewModel @Inject constructor (private val servicesRepository: ServicesRepository, private val appointmentsRepository: AppointmentsRepository) : ViewModel() {
    val allAppointments: StateFlow<List<Appointment>> = appointmentsRepository.getAppointments()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // Services are managed via SharedViewModel, but owner can add/edit/delete
    fun addService(name: String, description: String, price: Double, duration: Int) {
        viewModelScope.launch {
            if (name.isNotBlank() && description.isNotBlank() && price > 0) {
                servicesRepository.addService(
                    Service(
                        name = name,
                        description = description,
                        price = price,
                        duration = duration
                    )
                )
            }
        }
    }

    fun updateService(service: Service) {
        viewModelScope.launch {
            servicesRepository.updateService(service)
        }
    }

    fun deleteService(service: Service) {
        viewModelScope.launch {
            servicesRepository.removeService(service)
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            appointmentsRepository.removeAppointment(appointment)
        }
    }
    // Can add update appointment status here if needed
}