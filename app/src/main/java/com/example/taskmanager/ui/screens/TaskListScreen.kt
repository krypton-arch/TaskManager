package com.example.taskmanager.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FilterList
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanager.ui.components.AddEditTaskDialog
import com.example.taskmanager.ui.components.StatusChip
import com.example.taskmanager.ui.components.TaskCard
import com.example.taskmanager.ui.components.TopBar
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.LimeGreen
import com.example.taskmanager.ui.theme.MutedText
import com.example.taskmanager.ui.theme.PurpleAccent
import com.example.taskmanager.viewmodel.TaskViewModel

@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.allTasks.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<com.example.taskmanager.data.Task?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Top Bar
        TopBar(onAddClick = { showAddDialog = true })

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting
            item {
                Text(
                    text = "Manage your\ntask",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSurface,
                    lineHeight = 32.sp
                )
            }

            // Hero Next Task Card
            item {
                val nextTask = tasks.firstOrNull()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50))
                        .background(DarkSurface)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = nextTask?.title ?: "No upcoming tasks",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AccessTime,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = if (nextTask != null && nextTask.dueTime.isNotEmpty())
                                        "${nextTask.dueTime}" else "—",
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
                                contentDescription = "Go",
                                tint = DarkSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
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
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusChip("In progress", inProgressCount, LimeGreen)
                        StatusChip("In review", inReviewCount, PurpleAccent)
                        StatusChip("On hold", onHoldCount, Color.White, isOutlined = true)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(DarkSurface)
                        ) {
                            Icon(
                                Icons.Outlined.CalendarMonth,
                                contentDescription = "Calendar",
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
                                contentDescription = "Filter",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Task Cards
            if (tasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No tasks yet",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MutedText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap + to add your first task",
                                fontSize = 14.sp,
                                color = MutedText
                            )
                        }
                    }
                }
            } else {
                itemsIndexed(tasks, key = { _, task -> task.taskId }) { index, task ->
                    TaskCard(
                        task = task,
                        index = index,
                        onEdit = { editingTask = it },
                        onDelete = { viewModel.delete(it) }
                    )
                }
            }

            // Bottom spacing
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    // Add Dialog
    if (showAddDialog) {
        AddEditTaskDialog(
            onDismiss = { showAddDialog = false },
            onSave = { task ->
                viewModel.insert(task)
                showAddDialog = false
            }
        )
    }

    // Edit Dialog
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
