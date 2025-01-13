package com.example.medeaseclient.presentation.navGraph

import ClientRoutes
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.medeaseclient.domain.model.Bed
import com.example.medeaseclient.domain.model.ClientProfile
import com.example.medeaseclient.domain.model.Doctor
import com.example.medeaseclient.presentation.features.auth.SignInScreen
import com.example.medeaseclient.presentation.features.auth.SignUpScreen
import com.example.medeaseclient.presentation.features.doctorsAndBeds.AddDoctorsScreen
import com.example.medeaseclient.presentation.features.doctorsAndBeds.BedsScreen
import com.example.medeaseclient.presentation.features.doctorsAndBeds.DoctorsScreen
import com.example.medeaseclient.presentation.features.doctorsAndBeds.UpdateBedsScreen
import com.example.medeaseclient.presentation.features.helper.CommingSoonScreen
import com.example.medeaseclient.presentation.features.helper.HospitalProfileScreen
import com.example.medeaseclient.presentation.features.home.HomeScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf


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
                    navController.navigate(ClientRoutes.SignInScreen) {
                        popUpTo(ClientRoutes.SignInScreen) {
                            inclusive = true
                        }
                    }
                },
                hospitalProfile = ClientProfile(
                    hospitalId = profile.hospitalId,
                    hospitalName = profile.hospitalName,
                    hospitalEmail = profile.hospitalEmail,
                    hospitalPhone = profile.hospitalPhone,
                    hospitalCity = profile.hospitalCity,
                    hospitalPinCode = profile.hospitalPinCode
                ),
                onBackClick = { navController.navigateUp() },
            )
        }
        composable<ClientRoutes.AppointmentScreen> {
            CommingSoonScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ClientRoutes.DoctorScreen> { backStackEntry ->
            val hospital : ClientRoutes.DoctorScreen = backStackEntry.toRoute()
            DoctorsScreen(navController = navController, hospitalId = hospital.hospitalId)
        }
        composable<ClientRoutes.AddDoctorScreen>(
            typeMap = mapOf(
                typeOf<Doctor>() to CustomNavigationTypes.DoctorType
            )
        ) { backStackEntry ->
            val hospital : ClientRoutes.AddDoctorScreen = backStackEntry.toRoute()
            AddDoctorsScreen(navController = navController, doctor = hospital.doctor, hospitalId = hospital.hospitalId)
        }
        composable<ClientRoutes.BedScreen> { backStackEntry ->
            val hospital : ClientRoutes.BedScreen = backStackEntry.toRoute()
            BedsScreen(
                hospitalId = hospital.hospitalId,
                navController = navController
            )
        }
        composable<ClientRoutes.UpdateBedScreen>(
            typeMap = mapOf(
                typeOf<Bed>() to CustomNavigationTypes.BedType
            )
        ) { backStackEntry ->
            val hospital : ClientRoutes.UpdateBedScreen = backStackEntry.toRoute()
            UpdateBedsScreen(
                hospitalId = hospital.hospitalId,
                bed = hospital.bed,
                navController = navController
            )
        }
    }
}

private object CustomNavigationTypes {
    val DoctorType = object : NavType<Doctor>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Doctor? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Doctor {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Doctor): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Doctor) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    val BedType = object : NavType<Bed>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Bed? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Bed {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Bed): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Bed) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}