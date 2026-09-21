package com.example.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserSelectionScreen(onUserSelected: (UserType) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Who is using this device?", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { onUserSelected(UserType.PARENT) },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(text = "Parent")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onUserSelected(UserType.CHILD) },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(text = "Child")
        }
    }
}

@Composable
fun ParentDashboard(viewModel: ScreenTimeViewModel) {
    val availableTime by viewModel.availableTime.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Parent Dashboard", style = MaterialTheme.typography.headlineSmall)
            Row {
                Button(onClick = { viewModel.navigateTo(AppScreen.CONTENT) }) {
                    Text("Enter App")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { viewModel.resetUser() }) {
                    Text("Switch User")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Child's Available Screen Time: $availableTime minutes")
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Tasks and Rewards", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(viewModel.tasks) { task ->
                ListItem(
                    headlineContent = { Text(task.description) },
                    supportingContent = { Text("Reward: ${task.rewardMinutes} mins") },
                    trailingContent = {
                        if (task.isCompleted) {
                            Text("Completed", color = MaterialTheme.colorScheme.primary)
                        } else {
                            Text("Pending")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChildScreen(viewModel: ScreenTimeViewModel) {
    val availableTime by viewModel.availableTime.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Child's View", style = MaterialTheme.typography.headlineSmall)
            Row {
                if (availableTime > 0) {
                    Button(onClick = { viewModel.navigateTo(AppScreen.CONTENT) }) {
                        Text("Enter App")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Button(onClick = { viewModel.resetUser() }) {
                    Text("Lock")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Available Screen Time", style = MaterialTheme.typography.titleMedium)
                Text(text = "$availableTime", style = MaterialTheme.typography.displayLarge)
                Text(text = "minutes")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Complete tasks to earn time!", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(viewModel.tasks) { task ->
                ListItem(
                    headlineContent = { Text(task.description) },
                    supportingContent = { Text("Reward: ${task.rewardMinutes} mins") },
                    trailingContent = {
                        Button(
                            onClick = { viewModel.completeTask(task.id) },
                            enabled = !task.isCompleted
                        ) {
                            Text(if (task.isCompleted) "Done" else "Claim")
                        }
                    }
                )
            }
        }
    }
}
