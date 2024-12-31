package com.example.medease.presentation.navGraph

import Routes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.medease.presentation.features.allFeatures.HealthRecordsScreen
import com.example.medease.presentation.features.allFeatures.MyAppointmentsScreen
import com.example.medease.presentation.features.allFeatures.ProfileScreen
import com.example.medease.presentation.features.allFeatures.TransactionsScreen
import com.example.medease.presentation.features.auth.SignInScreen
import com.example.medease.presentation.features.auth.SignUpScreen
import com.example.medease.presentation.features.home.HomeScreen


@Composable
fun MedEaseNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: Routes
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Routes.SignUpScreen> {
            SignUpScreen(
                onSignInClick = {
                    navController.navigate(Routes.SignInScreen) {
                        popUpTo(Routes.SignInScreen) {
                            inclusive = true
                        }
                    }
                },
                onSuccessFullSignUp = { navController.navigate(Routes.HomeScreen) },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<Routes.SignInScreen> {
            SignInScreen(
                onSignUpClick = { navController.navigate(Routes.SignUpScreen) },
                onSuccessFullLogin = { navController.navigate(Routes.HomeScreen) }
            )
        }
        composable<Routes.HomeScreen> {
            HomeScreen(navController = navController)
        }
        composable<Routes.MyAppointmentsScreen> { backStackEntry ->
            val user: Routes.MyAppointmentsScreen = backStackEntry.toRoute()
            MyAppointmentsScreen(navController = navController)
        }
        composable<Routes.HealthRecordsScreen> { backStackEntry ->
            val user: Routes.HealthRecordsScreen = backStackEntry.toRoute()
            HealthRecordsScreen(navController = navController)
        }
        composable<Routes.TransactionsScreen> { backStackEntry ->
            val user: Routes.TransactionsScreen = backStackEntry.toRoute()
            TransactionsScreen(navController = navController)
        }
        composable<Routes.ProfileScreen> { backStackEntry ->
            val user: Routes.ProfileScreen = backStackEntry.toRoute()
            ProfileScreen(navController = navController)
        }
    }
}