package smartbeautysalontest.ui.screens.customer

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.smart_beauty_salon.ui.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerPreferencesScreen(
    navController: NavController,
    customerViewModel: CustomerViewModel
) {
    val preferences by customerViewModel.customerPreferences.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var hairType by remember { mutableStateOf("") }
    var skinType by remember { mutableStateOf("") }
    var preferredStylist by remember { mutableStateOf("") }

    // Update local state when preferences are loaded or changed
    LaunchedEffect(preferences) {
        preferences?.let {
            hairType = it.hairType
            skinType = it.skinType
            preferredStylist = it.preferredStylist
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Preferences") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Set your preferences to help us tailor your experience.",
                style = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                value = hairType,
                onValueChange = { hairType = it },
                label = { Text("Hair Type (e.g., Oily, Dry, Curly)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = skinType,
                onValueChange = { skinType = it },
                label = { Text("Skin Type (e.g., Sensitive, Combination)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = preferredStylist,
                onValueChange = { preferredStylist = it },
                label = { Text("Preferred Stylist (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    customerViewModel.savePreferences(hairType, skinType, preferredStylist)
                    Toast.makeText(context, "Preferences saved!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Preferences")
            }
        }
    }
}