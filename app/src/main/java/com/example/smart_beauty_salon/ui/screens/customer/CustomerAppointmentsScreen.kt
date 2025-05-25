package smartbeautysalontest.ui.screens.customer

import android.icu.text.NumberFormat
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
import com.example.smart_beauty_salon.data.model.Service
import com.example.smart_beauty_salon.ui.viewmodels.CustomerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerAppointmentsScreen(
    navController: NavController,
    customerViewModel: CustomerViewModel,
    sharedViewModel: SharedViewModel
) {
    val appointments by customerViewModel.customerAppointments.collectAsStateWithLifecycle()
    val allServices by sharedViewModel.allServices.collectAsStateWithLifecycle()
    val servicesMap = remember(allServices) { allServices.associateBy { it.id } }

    var showDialog by remember { mutableStateOf(false) }
    var appointmentToCancel by remember { mutableStateOf<Appointment?>(null) }

    if (showDialog && appointmentToCancel != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cancel Appointment") },
            text = { Text("Are you sure you want to cancel this appointment?") },
            confirmButton = {
                Button(onClick = {
                    appointmentToCancel?.let { customerViewModel.cancelAppointment(it) }
                    showDialog = false
                    appointmentToCancel = null
                }) {
                    Text("Yes, Cancel")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Appointments", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        if (appointments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "You have no upcoming appointments.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(appointments) { appointment ->
                    val service = servicesMap[appointment.serviceId]
                    AppointmentItemCard(
                        appointment = appointment,
                        service = service,
                        onCancelClick = {
                            appointmentToCancel = appointment
                            showDialog = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentItemCard(
    appointment: Appointment,
    service: Service?,
    onCancelClick: () -> Unit,
    showCancelButton: Boolean = true
) {
    val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = service?.name ?: "Service ID: ${appointment.serviceId}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (showCancelButton) {
                    IconButton(onClick = onCancelClick) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Cancel Appointment",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Date: ${dateFormat.format(Date(appointment.appointmentDateTime))}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (appointment.customerNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Notes: ${appointment.customerNotes}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            service?.price?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Price: ${
                        NumberFormat.getCurrencyInstance(Locale.getDefault()).format(it)
                    }",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}