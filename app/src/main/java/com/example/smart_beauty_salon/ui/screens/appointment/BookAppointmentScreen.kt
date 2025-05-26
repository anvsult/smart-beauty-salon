package com.example.smart_beauty_salon.ui.screens.appointment

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.smart_beauty_salon.ui.navigation.Screen
import com.example.smart_beauty_salon.ui.viewmodels.CustomerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    navController: NavController,
    serviceId: UUID,
    customerViewModel: CustomerViewModel,
    sharedViewModel: SharedViewModel
) {
    val service by sharedViewModel.getServiceById(serviceId).collectAsStateWithLifecycle(initialValue = null)
    val context = LocalContext.current
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    val coroutineScope = rememberCoroutineScope()

    val selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    val selectedTime by remember { mutableStateOf(Calendar.getInstance()) }
    var notes by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf<String?>(null) }

    val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    var formattedTime by remember { mutableStateOf(timeFormat.format(selectedTime.time)) }
    var formattedDate by remember { mutableStateOf(dateFormat.format(selectedDate.time)) }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker(
            initialDate = selectedDate.toInstant().atZone(selectedDate.timeZone.toZoneId()).toLocalDate(),
            title = "Pick a date"
        ) { date ->
            selectedDate.set(Calendar.YEAR, date.year)
            selectedDate.set(Calendar.MONTH, date.monthValue - 1)
            selectedDate.set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
            formattedDate = dateFormat.format(selectedDate.time)
        }
    }

    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        timepicker(
            initialTime = selectedTime.toInstant().atZone(selectedTime.timeZone.toZoneId()).toLocalTime(),
            title = "Pick a time",
            is24HourClock = true
        ) { time ->
            selectedTime.set(Calendar.HOUR_OF_DAY, time.hour)
            selectedTime.set(Calendar.MINUTE, time.minute)
            formattedTime = timeFormat.format(selectedTime.time)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Appointment", style = MaterialTheme.typography.titleLarge) },
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
            service?.let { s ->
                Text("Booking: ${s.name}", style = MaterialTheme.typography.headlineSmall)
                Text("Description: ${s.description}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Price: ${currencyFormat.format(s.price)}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Date Picker
                OutlinedTextField(
                    value = formattedDate,
                    onValueChange = {  },
                    label = { Text("Appointment Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { dateDialogState.show() }) {
                            Icon(Icons.Filled.DateRange, "Select Date")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Time Picker
                OutlinedTextField(
                    value = formattedTime,
                    onValueChange = { /* Read-only, changed by dialog */ },
                    label = { Text("Appointment Time") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { timeDialogState.show() }) {
                            Icon(Icons.Filled.Edit, "Select Time") // Using Edit icon as placeholder
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Additional Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Button(
                    onClick = {
                        val appointmentDateTime = Calendar.getInstance().apply {
                            timeInMillis = selectedDate.timeInMillis
                            set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY))
                            set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE))
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }

                        if (appointmentDateTime.timeInMillis <= System.currentTimeMillis()) {
                            showErrorMessage = "Please select a future date and time."
                            return@Button
                        }

                        coroutineScope.launch {
                            customerViewModel.bookAppointment(s.id, appointmentDateTime.timeInMillis, notes)
                                .collect { bookingId ->
                                    when {
                                        bookingId != null && bookingId > 0 -> {
                                            Toast.makeText(context, "Appointment booked successfully!", Toast.LENGTH_LONG).show()
                                            navController.popBackStack(Screen.CustomerHome.route, false)
                                        }
                                        bookingId?.toInt() == 0 -> {
                                            showErrorMessage = "Booking failed. Please try again."
                                        }
                                    }
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirm Booking")
                }

                if (showSuccessMessage) {
                    LaunchedEffect(Unit) { // To show toast once
                        Toast.makeText(context, "Appointment booked successfully!", Toast.LENGTH_LONG).show()
                        navController.popBackStack(Screen.CustomerHome.route, false)
                        showSuccessMessage = false // Reset
                    }
                }
                showErrorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                    LaunchedEffect(it) { // Auto clear message or use Snackbar
                        kotlinx.coroutines.delay(3000)
                        showErrorMessage = null
                    }
                }


            } ?: run {
                CircularProgressIndicator()
                Text("Loading service details...")
            }
        }
    }
}