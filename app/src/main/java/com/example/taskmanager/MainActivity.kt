package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.ui.navigation.BottomNavGraph
import com.example.taskmanager.ui.theme.TaskmanagerTheme
import com.example.taskmanager.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val database = TaskDatabase.getDatabase(applicationContext)
        val repository = TaskRepository(database.taskDao())
        val viewModel = ViewModelProvider(
            this,
            TaskViewModel.Factory(repository)
        )[TaskViewModel::class.java]

        setContent {
            TaskmanagerTheme {
                BottomNavGraph(viewModel = viewModel)
            }
        }
    }
}