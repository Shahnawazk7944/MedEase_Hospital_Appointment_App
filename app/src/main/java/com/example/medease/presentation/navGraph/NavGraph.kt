package com.example.medease.presentation.navGraph

import Routes
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.medease.domain.model.AppointmentDetails
import com.example.medease.domain.model.UserProfile
import com.example.medease.presentation.features.allFeatures.AppointmentDetailsScreen
import com.example.medease.presentation.features.allFeatures.BookingSuccessScreen
import com.example.medease.presentation.features.allFeatures.HealthRecordsScreen
import com.example.medease.presentation.features.allFeatures.MyAppointmentsScreen
import com.example.medease.presentation.features.allFeatures.PaymentScreen
import com.example.medease.presentation.features.allFeatures.TransactionsScreen
import com.example.medease.presentation.features.allFeatures.UserProfileScreen
import com.example.medease.presentation.features.auth.ForgotPasswordScreen
import com.example.medease.presentation.features.auth.SignInScreen
import com.example.medease.presentation.features.auth.SignUpScreen
import com.example.medease.presentation.features.home.HomeScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf


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
                onSuccessFullLogin = { navController.navigate(Routes.HomeScreen) },
                onForgotPasswordClick = { navController.navigate(Routes.ForgotPasswordScreen) }
            )
        }
        composable<Routes.ForgotPasswordScreen> {
            ForgotPasswordScreen(
               navController = navController
            )
        }
        composable<Routes.HomeScreen> {
            HomeScreen(navController = navController)
        }
        composable<Routes.MyAppointmentsScreen> { backStackEntry ->
            val user: Routes.MyAppointmentsScreen = backStackEntry.toRoute()
            MyAppointmentsScreen(navController = navController, userId = user.userId)
        }
        composable<Routes.HealthRecordsScreen> { backStackEntry ->
            val user: Routes.HealthRecordsScreen = backStackEntry.toRoute()
            HealthRecordsScreen(navController = navController, userId = user.userId)
        }
        composable<Routes.TransactionsScreen> { backStackEntry ->
            val user: Routes.TransactionsScreen = backStackEntry.toRoute()
            TransactionsScreen(navController = navController, userId = user.userId)
        }
        composable<Routes.ProfileScreen> { backStackEntry ->
            val user: Routes.ProfileScreen = backStackEntry.toRoute()
            UserProfileScreen(
                userProfile = UserProfile(
                    userId = user.userId,
                    name = user.name,
                    email = user.email,
                    phone = user.phone
                ),
                onLogoutClick = {
                    Log.d("----", "logout clicked")
                    navController.navigate(Routes.SignInScreen) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                    Log.d("----", " logout clicked passed ")
                },
                onBackClick = { navController.navigateUp() }
            )
        }

        composable<Routes.PaymentScreen>(
            typeMap = mapOf(
                typeOf<AppointmentDetails>() to CustomNavigationTypes.AppointmentDetailsType
            )
        ) { backStackEntry ->
            val user: Routes.PaymentScreen = backStackEntry.toRoute()
            PaymentScreen(
                appointmentDetails = user.appointmentDetails,
                navController = navController
            )
        }
        composable<Routes.BookingSuccessScreen> { backStackEntry ->
            val user: Routes.BookingSuccessScreen = backStackEntry.toRoute()
            BookingSuccessScreen(
                appointmentId = user.appointmentId,
                transactionId = user.transactionId,
                userId = user.userId,
                navController = navController
            )
        }
        composable<Routes.AppointmentDetailsScreen> { backStackEntry ->
            val user: Routes.AppointmentDetailsScreen = backStackEntry.toRoute()
            AppointmentDetailsScreen(
                appointmentId = user.appointmentId,
                navController = navController
            )
        }
    }
}

private object CustomNavigationTypes {
    val AppointmentDetailsType = object : NavType<AppointmentDetails>(isNullableAllowed = true) {
        override fun get(bundle: Bundle, key: String): AppointmentDetails? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): AppointmentDetails {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: AppointmentDetails): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: AppointmentDetails) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}