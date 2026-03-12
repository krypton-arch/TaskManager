package com.example.taskmanager.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanager.data.Task
import com.example.taskmanager.ui.components.AddEditTaskDialog
import com.example.taskmanager.ui.components.StatusChip
import com.example.taskmanager.ui.components.TaskCard
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.LimeGreen
import com.example.taskmanager.ui.theme.MutedText
import com.example.taskmanager.ui.theme.PurpleAccent
import com.example.taskmanager.ui.theme.Spacing
import com.example.taskmanager.ui.theme.TaskmanagerTheme
import com.example.taskmanager.viewmodel.TaskUiState
import com.example.taskmanager.viewmodel.TaskViewModel

@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var isSearchActive by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        // Search Bar Toggleable
        AnimatedVisibility(
            visible = isSearchActive,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
                placeholder = { Text("Search tasks...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.onSearchQueryChange("")
                        isSearchActive = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Close search")
                    }
                },
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurpleAccent,
                    unfocusedBorderColor = MutedText.copy(alpha = 0.5f)
                ),
                singleLine = true
            )
        }

        when (val state = uiState) {
            is TaskUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PurpleAccent)
                }
            }
            is TaskUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.message}", color = MutedText)
                }
            }
            is TaskUiState.Success -> {
                TaskListContent(
                    tasks = state.tasks,
                    selectedStatus = selectedStatus,
                    onStatusFilterClick = { viewModel.onStatusFilterChange(it) },
                    onSearchClick = { isSearchActive = !isSearchActive },
                    onAddClick = { showAddDialog = true },
                    onEdit = { editingTask = it },
                    onDelete = { viewModel.delete(it) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddEditTaskDialog(
            onDismiss = { showAddDialog = false },
            onSave = { task ->
                viewModel.insert(task)
                showAddDialog = false
            }
        )
    }

    editingTask?.let { task ->
        AddEditTaskDialog(
            task = task,
            onDismiss = { editingTask = null },
            onSave = { updated ->
                viewModel.update(updated)
                editingTask = null
            }
        )
    }
}

@Composable
private fun TaskListContent(
    tasks: List<Task>,
    selectedStatus: String?,
    onStatusFilterClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onAddClick: () -> Unit,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedCount by animateIntAsState(
        targetValue = tasks.size,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "taskCount"
    )

    LazyColumn(
        contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = modifier.fillMaxSize()
    ) {
        // Greeting
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Manage your\ntask",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSurface,
                    lineHeight = 34.sp,
                    letterSpacing = (-0.25).sp
                )
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DarkSurface.copy(alpha = 0.05f))
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = DarkSurface)
                }
            }
            Spacer(modifier = Modifier.height(Spacing.md))
        }

        // Hero Next Task Card
        item {
            val nextTask = tasks.firstOrNull { it.status == "In progress" } ?: tasks.firstOrNull()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 80.dp)
                    .clip(RoundedCornerShape(50))
                    .background(DarkSurface)
                    .padding(horizontal = Spacing.lg, vertical = Spacing.md)
                    .animateContentSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = nextTask?.title ?: "No upcoming tasks",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                            modifier = Modifier.padding(top = Spacing.xxs)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AccessTime,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (nextTask != null && nextTask.dueTime.isNotEmpty())
                                    nextTask.dueTime else "—",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LimeGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Go to task",
                            tint = DarkSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Spacing.lg))
        }

        // Hero count
        item {
            Text(
                text = "You have $animatedCount tasks showing",
                fontSize = 14.sp,
                color = MutedText,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
        }

        // Status Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.weight(1f)
                ) {
                    StatusChip(
                        label = "In progress",
                        count = -1,
                        chipColor = LimeGreen,
                        isSelected = selectedStatus == "In progress",
                        onClick = { onStatusFilterClick("In progress") }
                    )
                    StatusChip(
                        label = "In review",
                        count = -1,
                        chipColor = PurpleAccent,
                        isSelected = selectedStatus == "In review",
                        onClick = { onStatusFilterClick("In review") }
                    )
                    StatusChip(
                        label = "On hold",
                        count = -1,
                        chipColor = Color.White,
                        isOutlined = true,
                        isSelected = selectedStatus == "On hold",
                        onClick = { onStatusFilterClick("On hold") }
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DarkSurface)
                    ) {
                        Icon(
                            Icons.Outlined.CalendarMonth,
                            contentDescription = "Calendar view",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Spacing.lg))
        }

        if (tasks.isEmpty()) {
            item {
                EmptyStateView(isSearch = true)
            }
        } else {
            // Task Cards
            itemsIndexed(tasks, key = { _, task -> task.taskId }) { index, task ->
                TaskCard(
                    task = task,
                    index = index,
                    onEdit = onEdit,
                    onDelete = onDelete
                )
            }
        }

        // Bottom spacing
        item { Spacer(modifier = Modifier.height(Spacing.md)) }
    }
}

@Composable
private fun EmptyStateView(isSearch: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = if (isSearch) Icons.Default.Search else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = LimeGreen,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            Text(
                text = if (isSearch) "No matching tasks" else "No tasks yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkSurface
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            Text(
                text = if (isSearch) "Try a different search term" else "Tap + to add your first task!",
                fontSize = 14.sp,
                color = MutedText,
                textAlign = TextAlign.Center
            )
        }
    }
}
