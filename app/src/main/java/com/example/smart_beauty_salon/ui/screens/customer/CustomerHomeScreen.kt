package com.example.smartbeautysalontest.ui.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_beauty_salon.ui.navigation.Screen
import com.example.smart_beauty_salon.ui.viewmodels.CustomerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    navController: NavController,
    customerViewModel: CustomerViewModel,
    sharedViewModel: SharedViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome, Customer",
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Explore services, manage bookings, and personalize your experience.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.ServiceCatalog.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Browse Services", fontSize = 16.sp)
            }

            Button(
                onClick = { navController.navigate(Screen.CustomerAppointments.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("My Appointments", fontSize = 16.sp)
            }

            Button(
                onClick = { navController.navigate(Screen.CustomerPreferences.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("My Preferences", fontSize = 16.sp)
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
                Text("Change Role", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}