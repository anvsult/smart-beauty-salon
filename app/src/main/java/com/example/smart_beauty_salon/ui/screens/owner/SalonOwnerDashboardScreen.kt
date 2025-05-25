package smartbeautysalontest.ui.screens.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_beauty_salon.ui.navigation.Screen
import com.example.smart_beauty_salon.ui.viewmodels.SalonOwnerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonOwnerDashboardScreen(
    navController: NavController,
    salonOwnerViewModel: SalonOwnerViewModel,
    sharedViewModel: SharedViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Salon Dashboard",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Welcome, Salon Owner",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Manage your services and appointments with ease.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate(Screen.ManageAppointments.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Manage Appointments", fontSize = 16.sp)
            }

            Button(
                onClick = { navController.navigate(Screen.ManageServices.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("Manage Services", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.RoleSelection.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Change Role / Logout", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}