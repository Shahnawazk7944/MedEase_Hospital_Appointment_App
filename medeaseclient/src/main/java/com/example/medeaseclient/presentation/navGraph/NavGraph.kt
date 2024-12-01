package com.example.medeaseclient.presentation.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medeaseclient.presentation.features.auth.SignInScreen
import com.example.medeaseclient.presentation.features.auth.SignUpScreen
import com.example.medeaseclient.presentation.features.auth.viewmodels.AuthViewModel
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
                onLogoutClick = {
                    navController.navigate(ClientRoutes.SignInScreen) {
                        popUpTo(ClientRoutes.SignInScreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }


    }
}