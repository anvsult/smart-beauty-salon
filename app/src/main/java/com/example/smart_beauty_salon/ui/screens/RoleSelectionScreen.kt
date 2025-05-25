package com.example.smartbeautysalontest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_beauty_salon.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectionScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Choose Your Role",
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to SmartBeauty",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = "Please select your role to continue.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { navController.navigate(Screen.CustomerHome.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("I am a Customer", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { navController.navigate(Screen.SalonOwnerHome.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("I am a Salon Owner", fontSize = 16.sp)
            }
        }
    }
}