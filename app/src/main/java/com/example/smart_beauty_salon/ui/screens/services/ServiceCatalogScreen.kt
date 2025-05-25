package smartbeautysalontest.ui.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_beauty_salon.data.model.Service
import com.example.smart_beauty_salon.ui.navigation.Screen
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCatalogScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val services by sharedViewModel.allServices.collectAsState()
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Our Services",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
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
        if (services.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No services available.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(services) { service ->
                    ServiceCatalogItem(
                        service = service,
                        currencyFormat = currencyFormat,
                        onBookClick = {
                            navController.navigate(Screen.BookAppointment.createRoute(service.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceCatalogItem(
    service: Service,
    currencyFormat: NumberFormat,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = service.name,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
            )
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = currencyFormat.format(service.price),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Button(
                onClick = onBookClick,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Book Now")
            }
        }
    }
}