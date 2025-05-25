package smartbeautysalontest.ui.screens.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.smart_beauty_salon.data.model.Appointment
import smartbeautysalontest.ui.screens.customer.AppointmentItemCard // Re-using the card
import com.example.smart_beauty_salon.ui.viewmodels.SalonOwnerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonManageAppointmentsScreen(
    navController: NavController,
    salonOwnerViewModel: SalonOwnerViewModel,
    sharedViewModel: SharedViewModel
) {
    val appointments by salonOwnerViewModel.allAppointments.collectAsStateWithLifecycle()
    val allServices by sharedViewModel.allServices.collectAsStateWithLifecycle()
    val servicesMap = remember(allServices) { allServices.associateBy { it.id } }

    var showDialog by remember { mutableStateOf(false) }
    var appointmentToDelete by remember { mutableStateOf<Appointment?>(null) }

    if (showDialog && appointmentToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Appointment") },
            text = { Text("Are you sure you want to delete this appointment? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        appointmentToDelete?.let { salonOwnerViewModel.deleteAppointment(it) }
                        showDialog = false
                        appointmentToDelete = null
                    }
                ) { Text("Yes, Delete") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) { Text("No") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage All Appointments") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        if (appointments.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No appointments booked yet.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(appointments) { appointment ->
                    val service = servicesMap[appointment.serviceId]
                    // Re-using AppointmentItemCard, but the "cancel" button acts as "delete" for owner
                    AppointmentItemCard(
                        appointment = appointment,
                        service = service,
                        onCancelClick = { // Semantically "delete" for owner
                            appointmentToDelete = appointment
                            showDialog = true
                        },
                        showCancelButton = true // Owner can delete
                    )
                }
            }
        }
    }
}