package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test.ui.theme.TestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                val viewModel: ScreenTimeViewModel = viewModel()
                val userType by viewModel.userType.collectAsState()
                val currentScreen by viewModel.currentScreen.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    if (currentScreen == AppScreen.CONTENT) {
                        Column {
                            val time by viewModel.availableTime.collectAsState()
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }) {
                                    Text("Back")
                                }
                                if (userType == UserType.CHILD) {
                                    Text("Time remaining: $time mins")
                                    Button(onClick = { viewModel.useTime(5) }) {
                                        Text("Use 5 mins")
                                    }
                                } else {
                                    Text("Parent Mode (Unlimited)")
                                }
                            }
                            BakingScreen()
                        }
                    } else {
                        when (userType) {
                            UserType.NONE -> UserSelectionScreen { viewModel.selectUser(it) }
                            UserType.PARENT -> ParentDashboard(viewModel)
                            UserType.CHILD -> ChildScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}