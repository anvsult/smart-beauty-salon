package smartbeautysalontest.ui.screens.owner

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.smart_beauty_salon.data.model.Service
import com.example.smart_beauty_salon.ui.viewmodels.SalonOwnerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditServiceScreen(
    navController: NavController,
    serviceId: UUID?, // Null if adding new service
    salonOwnerViewModel: SalonOwnerViewModel,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val isEditing = serviceId != null
    val screenTitle = if (isEditing) "Edit Service" else "Add New Service"

    var serviceName by remember { mutableStateOf("") }
    var serviceDescription by remember { mutableStateOf("") }
    var servicePriceString by remember { mutableStateOf("") }
    var serviceDuration by remember { mutableStateOf(30) } // Default duration, can be changed later

    var nameError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var durationError by remember { mutableStateOf<String?>(null) }

    // Load service details if editing
    if (isEditing && serviceId != null) {
        val serviceToEdit by sharedViewModel.getServiceById(serviceId).collectAsStateWithLifecycle(initialValue = null)
        LaunchedEffect(serviceToEdit) {
            serviceToEdit?.let {
                serviceName = it.name
                serviceDescription = it.description
                servicePriceString = it.price.toString()
                serviceDuration = it.duration
            }
        }
    }

    fun validateFields(): Boolean {
        var isValid = true
        nameError = if (serviceName.isBlank()) { isValid = false; "Service name cannot be empty" } else null
        descriptionError = if (serviceDescription.isBlank()) { isValid = false; "Description cannot be empty" } else null
        priceError = if (servicePriceString.isBlank()) {
            isValid = false; "Price cannot be empty"
        } else if (servicePriceString.toDoubleOrNull() == null || servicePriceString.toDouble() <= 0) {
            isValid = false; "Please enter a valid positive price"
        } else null
        durationError = if (serviceDuration <= 0) {
            isValid = false; "Duration must be a positive number"
        } else null
        return isValid
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = serviceName,
                onValueChange = { serviceName = it; nameError = null },
                label = { Text("Service Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError != null,
                singleLine = true
            )
            if (nameError != null) {
                Text(text = nameError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = serviceDescription,
                onValueChange = { serviceDescription = it; descriptionError = null },
                label = { Text("Service Description") },
                modifier = Modifier.fillMaxWidth(),
                isError = descriptionError != null,
                maxLines = 3
            )
            if (descriptionError != null) {
                Text(text = descriptionError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = servicePriceString,
                onValueChange = { servicePriceString = it; priceError = null },
                label = { Text("Service Price") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = priceError != null,
                singleLine = true
            )
            if (priceError != null) {
                Text(text = priceError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = serviceDuration.toString(),
                onValueChange = {
                    serviceDuration = it.toIntOrNull() ?: 0
                    durationError = null
                },
                label = { Text("Service Duration (minutes)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = durationError != null,
                singleLine = true
            )
            if (durationError != null) {
                Text(
                    text = durationError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (validateFields()) {
                        val price = servicePriceString.toDouble() // Already validated
                        if (isEditing && serviceId != null) {
                            salonOwnerViewModel.updateService(
                                Service(
                                    id = serviceId,
                                    name = serviceName,
                                    description = serviceDescription,
                                    price = price,
                                    duration = serviceDuration
                                )
                            )
                            Toast.makeText(context, "Service updated successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            salonOwnerViewModel.addService(
                                name = serviceName,
                                description = serviceDescription,
                                price = price,
                                duration = serviceDuration
                            )
                            Toast.makeText(context, "Service added successfully!", Toast.LENGTH_SHORT).show()
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Save Changes" else "Add Service")
            }
        }
    }
}