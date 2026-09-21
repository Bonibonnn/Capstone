package com.example.test

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class UserType {
    NONE, PARENT, CHILD
}

data class Task(
    val id: Int,
    val description: String,
    val rewardMinutes: Int,
    var isCompleted: Boolean = false
)

enum class AppScreen {
    DASHBOARD, CONTENT
}

class ScreenTimeViewModel : ViewModel() {
    private val _userType = MutableStateFlow(UserType.NONE)
    val userType: StateFlow<UserType> = _userType.asStateFlow()

    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _availableTime = MutableStateFlow(0) // in minutes
    val availableTime: StateFlow<Int> = _availableTime.asStateFlow()

    val tasks = mutableStateListOf<Task>(
        Task(1, "Clean your room", 15),
        Task(2, "Finish homework", 30),
        Task(3, "Read a book for 20 mins", 20)
    )

    fun selectUser(type: UserType) {
        _userType.value = type
        _currentScreen.value = AppScreen.DASHBOARD
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun completeTask(taskId: Int) {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index != -1 && !tasks[index].isCompleted) {
            tasks[index] = tasks[index].copy(isCompleted = true)
            _availableTime.value += tasks[index].rewardMinutes
        }
    }
    
    fun useTime(minutes: Int) {
        _availableTime.value = (_availableTime.value - minutes).coerceAtLeast(0)
        if (_availableTime.value == 0 && _userType.value == UserType.CHILD) {
            _currentScreen.value = AppScreen.DASHBOARD
        }
    }

    fun resetUser() {
        _userType.value = UserType.NONE
        _currentScreen.value = AppScreen.DASHBOARD
    }
}
