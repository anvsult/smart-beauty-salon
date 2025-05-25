package com.example.smart_beauty_salon.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel // Import for Hilt ViewModels
import com.example.smart_beauty_salon.SmartSalonApp
import com.example.smart_beauty_salon.ui.screens.appointment.BookAppointmentScreen


import com.example.smart_beauty_salon.ui.viewmodels.CustomerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SalonOwnerViewModel
import com.example.smart_beauty_salon.ui.viewmodels.SharedViewModel
import com.example.smartbeautysalontest.ui.screens.RoleSelectionScreen
import com.example.smartbeautysalontest.ui.screens.customer.CustomerHomeScreen
import smartbeautysalontest.ui.screens.customer.CustomerAppointmentsScreen
import smartbeautysalontest.ui.screens.customer.CustomerPreferencesScreen
import smartbeautysalontest.ui.screens.customer.ServiceCatalogScreen
import smartbeautysalontest.ui.screens.owner.AddEditServiceScreen
import smartbeautysalontest.ui.screens.owner.SalonManageAppointmentsScreen
import smartbeautysalontest.ui.screens.owner.SalonManageServicesScreen
import smartbeautysalontest.ui.screens.owner.SalonOwnerDashboardScreen
import java.util.UUID

sealed class Screen(val route: String) {
    object RoleSelection : Screen("role_selection")
    object CustomerHome : Screen("customer_home")
    object ServiceCatalog : Screen("service_catalog")

    object BookAppointment : Screen("book_appointment/{serviceId}") {
        fun createRoute(serviceId: UUID) = "book_appointment/$serviceId"
    }

    object CustomerAppointments : Screen("customer_appointments")
    object CustomerPreferences : Screen("customer_preferences")
    object SalonOwnerHome : Screen("salon_owner_home")
    object ManageAppointments : Screen("manage_appointments")
    object ManageServices : Screen("manage_services")

    object AddEditService : Screen("add_edit_service?serviceId={serviceId}") {
        fun createRoute(serviceId: UUID? = null) =
            if (serviceId != null) "add_edit_service?serviceId=$serviceId" else "add_edit_service"
    }
}


@Composable
fun AppNavHost(
    navController: NavHostController,
    application: SmartSalonApp, // Uncomment if you need to pass the application context
    // No need to pass application or viewModelFactory if using Hilt correctly
) {
    NavHost(navController = navController, startDestination = Screen.RoleSelection.route) {
        composable(Screen.RoleSelection.route) {
            // Assuming RoleSelectionScreen doesn't need a ViewModel or gets it internally
            RoleSelectionScreen(navController)
        }

        // --- Customer Routes ---
        composable(Screen.CustomerHome.route) {
            // Hilt will provide these ViewModels
            val customerViewModel: CustomerViewModel = hiltViewModel()
            val sharedViewModel: SharedViewModel = hiltViewModel()
            CustomerHomeScreen(navController, customerViewModel, sharedViewModel)
        }
        composable(Screen.ServiceCatalog.route) {
            val sharedViewModel: SharedViewModel = hiltViewModel()
            ServiceCatalogScreen(navController, sharedViewModel)
        }
        composable(
            route = Screen.BookAppointment.route,
            arguments = listOf(navArgument("serviceId") {
                type = NavType.StringType // UUIDs are passed as Strings in routes
            })
        ) { backStackEntry ->
            val serviceIdString = backStackEntry.arguments?.getString("serviceId")
            // Convert String to UUID carefully
            val serviceId = try { serviceIdString?.let { UUID.fromString(it) } } catch (e: IllegalArgumentException) { null }

            val customerViewModel: CustomerViewModel = hiltViewModel()
            val sharedViewModel: SharedViewModel = hiltViewModel()
            if (serviceId != null) {
                BookAppointmentScreen(navController, serviceId, customerViewModel, sharedViewModel)
            } else {
                // Handle error: invalid UUID string or missing ID
                navController.popBackStack()
            }
        }
        composable(Screen.CustomerAppointments.route) {
            val customerViewModel: CustomerViewModel = hiltViewModel()
            val sharedViewModel: SharedViewModel = hiltViewModel() // If needed
            CustomerAppointmentsScreen(navController, customerViewModel, sharedViewModel)
        }
        composable(Screen.CustomerPreferences.route) {
            val customerViewModel: CustomerViewModel = hiltViewModel()
            CustomerPreferencesScreen(navController, customerViewModel)
        }

        // --- Salon Owner Routes ---
        composable(Screen.SalonOwnerHome.route) {
            val salonOwnerViewModel: SalonOwnerViewModel = hiltViewModel()
            val sharedViewModel: SharedViewModel = hiltViewModel()
            SalonOwnerDashboardScreen(navController, salonOwnerViewModel, sharedViewModel)
        }
        composable(Screen.ManageAppointments.route) {
            val salonOwnerViewModel: SalonOwnerViewModel = hiltViewModel()
            val sharedViewModel: SharedViewModel = hiltViewModel()
            SalonManageAppointmentsScreen(navController, salonOwnerViewModel, sharedViewModel)
        }
        composable(Screen.ManageServices.route) {
            val salonOwnerViewModel: SalonOwnerViewModel = hiltViewModel()
            val sharedViewModel: SharedViewModel = hiltViewModel()
            SalonManageServicesScreen(navController, salonOwnerViewModel, sharedViewModel)
        }
        composable(
            route = Screen.AddEditService.route,
            arguments = listOf(navArgument("serviceId") {
                type = NavType.StringType
                nullable = true // serviceId is optional
            })
        ) { backStackEntry ->
            val serviceIdString = backStackEntry.arguments?.getString("serviceId")
            val serviceId = try { serviceIdString?.let { UUID.fromString(it) } } catch (e: IllegalArgumentException) { null }

            val salonOwnerViewModel: SalonOwnerViewModel = hiltViewModel()
            val sharedViewModel: SharedViewModel = hiltViewModel()
            AddEditServiceScreen(navController, serviceId, salonOwnerViewModel, sharedViewModel)
        }
    }
}