package smartbeautysalontest.ui.screens.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.smart_beauty_salon.data.model.Service
import com.example.smart_beauty_salon.ui.navigation.Screen
import com.example.smart_beauty_salon.ui.viewmodels.SalonOwnerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonManageServicesScreen(
    navController: NavController,
    salonOwnerViewModel: SalonOwnerViewModel,
    sharedViewModel: SharedViewModel
) {
    val services by sharedViewModel.allServices.collectAsStateWithLifecycle()
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    var showDeleteDialog by remember { mutableStateOf(false) }
    var serviceToDelete by remember { mutableStateOf<Service?>(null) }

    if (showDeleteDialog && serviceToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Service") },
            text = { Text("Delete '${serviceToDelete?.name}'? This will remove it from all future bookings.") },
            confirmButton = {
                Button(
                    onClick = {
                        serviceToDelete?.let { salonOwnerViewModel.deleteService(it) }
                        showDeleteDialog = false
                        serviceToDelete = null
                    }
                ) { Text("Yes, Delete") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Services") },
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddEditService.createRoute(null))
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Service")
            }
        }
    ) { padding ->
        if (services.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No services added.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tap '+' to add a new service.")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(services) { service ->
                    SalonServiceItem(
                        service = service,
                        currencyFormat = currencyFormat,
                        onEditClick = {
                            navController.navigate(Screen.AddEditService.createRoute(service.id))
                        },
                        onDeleteClick = {
                            serviceToDelete = service
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SalonServiceItem(
    service: Service,
    currencyFormat: NumberFormat,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(service.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(service.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Price: ${currencyFormat.format(service.price)}", style = MaterialTheme.typography.bodyMedium)
            }
            Column {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}