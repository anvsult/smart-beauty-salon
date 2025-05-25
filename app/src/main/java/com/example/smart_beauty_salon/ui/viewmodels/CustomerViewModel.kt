package com.example.smart_beauty_salon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.TypeConverter
import com.example.smart_beauty_salon.data.model.Appointment
import com.example.smart_beauty_salon.data.model.CustomerPreferences
import com.example.smart_beauty_salon.repository.AppointmentsRepository
import com.example.smart_beauty_salon.repository.PreferencesRepository
import com.example.smart_beauty_salon.repository.ServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val servicesRepository: ServicesRepository,
    private val appointmentsRepository: AppointmentsRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _customerPreferences = MutableStateFlow<CustomerPreferences?>(null)
    val customerPreferences: StateFlow<CustomerPreferences?> = _customerPreferences.asStateFlow()

    // For this simple app, customer appointments are all appointments.
    // In a real app, you'd filter by a customer ID.
    val customerAppointments: StateFlow<List<Appointment>> = appointmentsRepository.getAppointments()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            preferencesRepository.getCustomerPreferences().collect {
                _customerPreferences.value = it ?: CustomerPreferences() // Ensure non-null
            }
        }
    }

    fun savePreferences(hairType: String, skinType: String, stylist: String) {
        viewModelScope.launch {
            val currentPrefs = _customerPreferences.value ?: CustomerPreferences()
            val updatedPrefs = currentPrefs.copy(
                hairType = hairType,
                skinType = skinType,
                preferredStylist = stylist
            )
            preferencesRepository.saveCustomerPreferences(updatedPrefs)
        }
    }

    fun bookAppointment(serviceId: UUID, dateTime: Long, notes: String = ""): Flow<Long?> = flow {
        val prefs = _customerPreferences.value
        val appointmentNotes = mutableListOf<String>()
        if (notes.isNotBlank()) appointmentNotes.add(notes)
        prefs?.let {
            if (it.hairType.isNotBlank()) appointmentNotes.add("Hair: ${it.hairType}")
            if (it.skinType.isNotBlank()) appointmentNotes.add("Skin: ${it.skinType}")
            if (it.preferredStylist.isNotBlank()) appointmentNotes.add("Stylist: ${it.preferredStylist}")
        }

        val appointment = Appointment(
            serviceId = serviceId,
            appointmentDateTime = dateTime,
            customerNotes = appointmentNotes.joinToString(", ")
        )
        val bookingId = appointmentsRepository.addAppointment(appointment)
        emit(bookingId) // Emit the booking ID
    }.flowOn(Dispatchers.IO)

    fun cancelAppointment(appointment: Appointment) {
        viewModelScope.launch {
            appointmentsRepository.removeAppointment(appointment)
        }
    }
}