package com.example.medeaseclient.presentation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.medeaseclient.domain.model.ClientProfile
import com.example.medeaseclient.presentation.features.auth.SignInScreen
import com.example.medeaseclient.presentation.features.auth.SignUpScreen
import com.example.medeaseclient.presentation.features.helper.CommingSoonScreen
import com.example.medeaseclient.presentation.features.helper.HospitalProfileScreen
import com.example.medeaseclient.presentation.features.home.HomeScreen


@Composable
fun MedEaseClientNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: ClientRoutes
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<ClientRoutes.SignUpScreen> {
            SignUpScreen(
                onSignInClick = {
                    navController.navigate(ClientRoutes.SignInScreen) {
                        popUpTo(ClientRoutes.SignInScreen) {
                            inclusive = true
                        }
                    }
                },
                onSuccessFullSignUp = { navController.navigate(ClientRoutes.HomeScreen) },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ClientRoutes.SignInScreen> {
            SignInScreen(
                onSignUpClick = { navController.navigate(ClientRoutes.SignUpScreen) },
                onSuccessFullLogin = { navController.navigate(ClientRoutes.HomeScreen) }
            )
        }
        composable<ClientRoutes.HomeScreen> {
            HomeScreen(
                navController = navController,
                onLogoutClick = {
                    navController.navigate(ClientRoutes.SignInScreen) {
                        popUpTo(ClientRoutes.SignInScreen) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable<ClientRoutes.ProfileScreen> {
            backStackEntry ->
            val profile: ClientRoutes.ProfileScreen = backStackEntry.toRoute()
            HospitalProfileScreen(
                onLogoutClick = {
                },
                hospitalProfile = ClientProfile(
                    hospitalName = profile.hospitalName,
                    hospitalEmail = profile.hospitalEmail,
                    hospitalPhone = profile.hospitalPhone,
                    hospitalCity = profile.hospitalCity,
                    hospitalPinCode = profile.hospitalPinCode
                ),
                onEditProfileClick = {},
                onBackClick = { navController.navigateUp() },
            )
        }
        composable<ClientRoutes.AppointmentScreen> {
            CommingSoonScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ClientRoutes.DoctorScreen> {
            CommingSoonScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ClientRoutes.DrugScreen> {
            CommingSoonScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ClientRoutes.PrescriptionScreen> {
            CommingSoonScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ClientRoutes.BedScreen> {
            CommingSoonScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ClientRoutes.CheckUpScreen> {
            CommingSoonScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ClientRoutes.CareScreen> {
            CommingSoonScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ClientRoutes.EmergencyScreen> {
            CommingSoonScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}