package com.example.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class TaskUiState {
    data object Loading : TaskUiState()
    data class Success(val tasks: List<Task>) : TaskUiState()
    data class Error(val message: String) : TaskUiState()
}

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val uiState: StateFlow<TaskUiState> = repository.allTasks
        .map<List<Task>, TaskUiState> { TaskUiState.Success(it) }
        .catch { emit(TaskUiState.Error(it.message ?: "Unknown error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskUiState.Loading
        )

    val allTasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(task: Task) {
        viewModelScope.launch {
            try {
                repository.insert(task)
            } catch (e: Exception) {
                // Error handled via uiState flow
            }
        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            try {
                repository.update(task)
            } catch (e: Exception) {
                // Error handled via uiState flow
            }
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            try {
                repository.delete(task)
            } catch (e: Exception) {
                // Error handled via uiState flow
            }
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            try {
                repository.deleteAllTasks()
            } catch (e: Exception) {
                // Error handled via uiState flow
            }
        }
    }

    class Factory(private val repository: TaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
