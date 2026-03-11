package com.example.taskmanager.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val tasks by viewModel.allTasks.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    when (val state = uiState) {
        is TaskUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PurpleAccent)
            }
        }
        is TaskUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.message}", color = MutedText)
            }
        }
        is TaskUiState.Success -> {
            TaskListContent(
                tasks = tasks,
                onAddClick = { showAddDialog = true },
                onEdit = { editingTask = it },
                onDelete = { viewModel.delete(it) },
                modifier = modifier
            )
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

    if (tasks.isEmpty()) {
        EmptyStateView()
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = modifier.fillMaxSize()
        ) {
            // Greeting
            item {
                Text(
                    text = "Manage your\ntask",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSurface,
                    lineHeight = 34.sp,
                    letterSpacing = (-0.25).sp
                )
                Spacer(modifier = Modifier.height(Spacing.md))
            }

            // Hero Next Task Card
            item {
                val nextTask = tasks.firstOrNull()
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
                    text = "You have $animatedCount tasks for today",
                    fontSize = 14.sp,
                    color = MutedText,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(Spacing.sm))
            }

            // Status Row
            item {
                val inProgressCount = tasks.count { it.status == "In progress" }
                val inReviewCount = tasks.count { it.status == "In review" }
                val onHoldCount = tasks.count { it.status == "On hold" }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        StatusChip("In progress", inProgressCount, LimeGreen)
                        StatusChip("In review", inReviewCount, PurpleAccent)
                        StatusChip("On hold", onHoldCount, Color.White, isOutlined = true)
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
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(DarkSurface)
                        ) {
                            Icon(
                                Icons.Outlined.FilterList,
                                contentDescription = "Filter tasks",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Spacing.lg))
            }

            // Task Cards
            itemsIndexed(tasks, key = { _, task -> task.taskId }) { index, task ->
                TaskCard(
                    task = task,
                    index = index,
                    onEdit = onEdit,
                    onDelete = onDelete,
                    modifier = Modifier.animateItem()
                )
            }

            // Bottom spacing
            item { Spacer(modifier = Modifier.height(Spacing.md)) }
        }
    }
}

@Composable
private fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = LimeGreen,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            Text(
                text = "No tasks yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkSurface
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            Text(
                text = "Tap + to add your first task!",
                fontSize = 14.sp,
                color = MutedText,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Empty")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Empty")
@Composable
private fun EmptyStatePreview() {
    TaskmanagerTheme {
        EmptyStateView()
    }
}
