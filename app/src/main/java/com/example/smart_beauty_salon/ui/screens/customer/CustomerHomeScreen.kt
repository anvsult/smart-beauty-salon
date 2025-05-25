package com.example.smartbeautysalontest.ui.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smart_beauty_salon.ui.navigation.Screen
import com.example.smart_beauty_salon.ui.viewmodels.CustomerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    navController: NavController,
    customerViewModel: CustomerViewModel, // Injected via factory
    sharedViewModel: SharedViewModel   // Injected via factory
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer Dashboard") },
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
            Button(
                onClick = { navController.navigate(Screen.ServiceCatalog.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Browse Services & Book Appointment")
            }
            Button(
                onClick = { navController.navigate(Screen.CustomerAppointments.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View My Appointments")
            }
            Button(
                onClick = { navController.navigate(Screen.CustomerPreferences.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("My Preferences")
            }
            Button(
                onClick = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.RoleSelection.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Role")
            }
        }
    }
}