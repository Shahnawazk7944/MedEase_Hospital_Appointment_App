package com.example.medease.presentation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medease.presentation.features.auth.SignInScreen
import com.example.medease.presentation.features.auth.SignUpScreen


@Composable
fun MedEaseNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SignInScreen
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
                onBackClick = {navController.navigateUp()}
            )
        }
        composable<Routes.SignInScreen> {
            SignInScreen(
                onSignUpClick = { navController.navigate(Routes.SignUpScreen) },
                onSuccessFullLogin = { navController.navigate(Routes.HomeScreen) }
            )
        }
        composable<Routes.HomeScreen> {

        }


    }
}