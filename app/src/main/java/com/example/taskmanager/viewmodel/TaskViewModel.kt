package com.example.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.notification.TaskReminderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TaskUiState {
    data object Loading : TaskUiState()
    data class Success(val tasks: List<Task>) : TaskUiState()
    data class Error(val message: String) : TaskUiState()
}

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val reminderManager: TaskReminderManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedStatus = MutableStateFlow<String?>(null)
    val selectedStatus = _selectedStatus.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _tasks = _searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.allTasks
            } else {
                repository.searchTasks(query)
            }
        }

    val uiState: StateFlow<TaskUiState> = combine(_tasks, _selectedStatus) { tasks, status ->
        val filteredTasks = if (status == null) {
            tasks
        } else {
            tasks.filter { it.status == status }
        }
        TaskUiState.Success(filteredTasks) as TaskUiState
    }
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

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onStatusFilterChange(status: String?) {
        _selectedStatus.value = if (_selectedStatus.value == status) null else status
    }

    fun insert(task: Task) {
        viewModelScope.launch {
            try {
                val rowId = repository.insert(task)
                // Schedule reminder for the new task using the generated ID
                reminderManager.scheduleReminder(task.copy(taskId = rowId.toInt()))
            } catch (e: Exception) {
                // Error handled via uiState flow
            }
        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            try {
                repository.update(task)
                // Update reminder
                reminderManager.scheduleReminder(task)
            } catch (e: Exception) {
                // Error handled via uiState flow
            }
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            try {
                repository.delete(task)
                reminderManager.cancelReminder(task.taskId)
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
}