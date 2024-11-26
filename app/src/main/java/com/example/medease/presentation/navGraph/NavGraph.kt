package com.example.medease.presentation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun MedEaseNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SignInScreen
    ) {
        composable<Routes.SignUpScreen> {

        }
        composable<Routes.SignInScreen> {
//            SignInScreen()
        }
        composable<Routes.HomeScreen> {

        }


    }
}