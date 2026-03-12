package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.ui.navigation.BottomNavGraph
import com.example.taskmanager.ui.theme.TaskmanagerTheme
import com.example.taskmanager.viewmodel.TaskViewModel
import com.example.taskmanager.viewmodel.UserPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val taskViewModel: TaskViewModel = hiltViewModel()
            val userPrefsViewModel: UserPreferencesViewModel = hiltViewModel()
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