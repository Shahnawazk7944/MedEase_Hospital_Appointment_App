package com.example.medease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.designsystem.theme.MedEaseTheme
import com.example.medease.data.repository.auth.UserDataStoreRepository
import com.example.medease.presentation.navGraph.MedEaseNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dataStoreRepository: UserDataStoreRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val initialDestination = getInitialDestination()
            MedEaseTheme {
                MedEaseNavGraph(startDestination = initialDestination)
            }
        }
    }

    private fun getInitialDestination(): Routes = runBlocking {
        return@runBlocking dataStoreRepository.getRememberMe().fold(
            { Routes.SignInScreen }, //if any error occurs
            { isRemembered -> if (isRemembered) Routes.HomeScreen else Routes.SignInScreen } //if no error occurs
        )
    }
}

