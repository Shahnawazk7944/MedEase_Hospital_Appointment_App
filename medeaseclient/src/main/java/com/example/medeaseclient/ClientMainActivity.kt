package com.example.medeaseclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.designsystem.theme.MedEaseTheme
import com.example.medeaseclient.presentation.navGraph.MedEaseClientNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedEaseTheme {
                MedEaseClientNavGraph()
            }
        }
    }
}
