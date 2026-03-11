package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.data.UserPreferencesRepository
import com.example.taskmanager.ui.navigation.BottomNavGraph
import com.example.taskmanager.ui.theme.TaskmanagerTheme
import com.example.taskmanager.viewmodel.TaskViewModel
import com.example.taskmanager.viewmodel.UserPreferencesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val database = TaskDatabase.getDatabase(applicationContext)
        val repository = TaskRepository(database.taskDao())
        val taskViewModel = ViewModelProvider(
            this,
            TaskViewModel.Factory(repository)
        )[TaskViewModel::class.java]

        val userPrefsRepository = UserPreferencesRepository(applicationContext)
        val userPrefsViewModel = ViewModelProvider(
            this,
            UserPreferencesViewModel.Factory(userPrefsRepository)
        )[UserPreferencesViewModel::class.java]

        setContent {
            val themePreference by userPrefsViewModel.themePreference.collectAsState()

            TaskmanagerTheme(themePreference = themePreference) {
                BottomNavGraph(
                    viewModel = taskViewModel,
                    userPrefsViewModel = userPrefsViewModel
                )
            }
        }
    }
}