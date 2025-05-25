package smartbeautysalontest.ui.screens.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smart_beauty_salon.ui.navigation.Screen
import com.example.smart_beauty_salon.ui.viewmodels.SalonOwnerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonOwnerDashboardScreen(
    navController: NavController,
    salonOwnerViewModel: SalonOwnerViewModel, // May not be used directly here, but good to have
    sharedViewModel: SharedViewModel // May not be used directly here
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Salon Owner Dashboard") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Manage Your Salon", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.ManageAppointments.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage Appointments")
            }
            Button(
                onClick = { navController.navigate(Screen.ManageServices.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage Services")
            }
            // You might add other owner-specific features here, like viewing analytics (future)

            Spacer(modifier = Modifier.weight(1f)) // Pushes button to bottom

            Button(
                onClick = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.RoleSelection.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Role / Logout")
            }
        }
    }
}