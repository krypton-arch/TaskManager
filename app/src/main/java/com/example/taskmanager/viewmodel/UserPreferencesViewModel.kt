package com.example.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserPreferencesViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    val userName: StateFlow<String> = repository.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Daniel")

    val avatarColor: StateFlow<String> = repository.avatarColor
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Purple")

    val themePreference: StateFlow<String> = repository.themePreference
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "System")

    fun updateUserName(name: String) {
        viewModelScope.launch { repository.updateUserName(name) }
    }

    fun updateAvatarColor(color: String) {
        viewModelScope.launch { repository.updateAvatarColor(color) }
    }

    fun updateThemePreference(theme: String) {
        viewModelScope.launch { repository.updateThemePreference(theme) }
    }

    class Factory(private val repository: UserPreferencesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserPreferencesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserPreferencesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
