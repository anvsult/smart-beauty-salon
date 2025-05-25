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

    // Sync UI state with ViewModel
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
                title = { Text("My Preferences", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tailor your experience by setting your beauty preferences.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = hairType,
                onValueChange = { hairType = it },
                label = { Text("Hair Type") },
                placeholder = { Text("e.g., Oily, Dry, Curly") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = skinType,
                onValueChange = { skinType = it },
                label = { Text("Skin Type") },
                placeholder = { Text("e.g., Sensitive, Combination") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = preferredStylist,
                onValueChange = { preferredStylist = it },
                label = { Text("Preferred Stylist (Optional)") },
                placeholder = { Text("e.g., Lisa, Andre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    customerViewModel.savePreferences(hairType, skinType, preferredStylist)
                    Toast.makeText(context, "Preferences saved!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Save Preferences")
            }
        }
    }
}