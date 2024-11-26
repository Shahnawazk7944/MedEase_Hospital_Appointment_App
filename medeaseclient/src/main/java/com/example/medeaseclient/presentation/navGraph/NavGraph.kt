package com.example.medeaseclient.presentation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medeaseclient.presentation.features.auth.SignInScreen
import com.example.medeaseclient.presentation.features.auth.SignUpScreen


@Composable
fun MedEaseClientNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ClientRoutes.SignInScreen
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
                onSuccessFullSignUp = { navController.navigate(ClientRoutes.HomeScreen) }
            )
        }
        composable<ClientRoutes.SignInScreen> {
            SignInScreen(
                onSignUpClick = { navController.navigate(ClientRoutes.SignUpScreen) },
                onSuccessFullLogin = { navController.navigate(ClientRoutes.HomeScreen) }
            )
        }
        composable<ClientRoutes.HomeScreen> {

        }


    }
}