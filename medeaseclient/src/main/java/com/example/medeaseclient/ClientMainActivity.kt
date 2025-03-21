package com.example.medeaseclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.designsystem.theme.MedEaseTheme
import com.example.medeaseclient.data.repository.auth.ClientDataStoreRepository
import com.example.medeaseclient.presentation.navGraph.MedEaseClientNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ClientMainActivity : ComponentActivity() {
    @Inject
    lateinit var dataStoreRepository: ClientDataStoreRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val initialDestination = getInitialDestination()
            MedEaseTheme {
                MedEaseClientNavGraph(startDestination = initialDestination)
            }
        }
    }

    private fun getInitialDestination(): ClientRoutes = runBlocking {
        return@runBlocking dataStoreRepository.getRememberMe().fold(
            { ClientRoutes.SignInScreen }, //if any error occurs
            { isRemembered -> if (isRemembered) ClientRoutes.HomeScreen else ClientRoutes.SignInScreen } //if no error occurs
        )
    }
}

