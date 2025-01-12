package com.example.medease.presentation.features.allFeatures

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.designsystem.theme.spacing
import com.example.medease.presentation.features.allFeatures.viewModels.HealthRecordsStates
import com.example.medease.presentation.features.allFeatures.viewModels.HealthRecordsViewModel
import com.example.medease.presentation.features.common.CustomTopBar

@Composable
fun HealthRecordsScreen(
    viewModel: HealthRecordsViewModel = hiltViewModel(),
    userId: String,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()


    HealthRecordsContent(
        state = state,
        onBackClick = {},
    )
}


@Composable
fun HealthRecordsContent(
    state: HealthRecordsStates,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { onBackClick.invoke() },
                title = {
                    Text(
                        text = "Health Records",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.mediumLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}
