package com.example.taskmanager.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanager.ui.components.AddEditTaskDialog
import com.example.taskmanager.ui.components.ScheduleTaskBlock
import com.example.taskmanager.ui.components.WeekCalendarRow
import com.example.taskmanager.ui.theme.CardGreen
import com.example.taskmanager.ui.theme.CardWhite
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.LimeGreen
import com.example.taskmanager.ui.theme.MutedText
import com.example.taskmanager.ui.theme.PurpleAccent
import com.example.taskmanager.ui.theme.Spacing
import com.example.taskmanager.ui.theme.TaskmanagerTheme
import com.example.taskmanager.viewmodel.TaskViewModel
import java.time.LocalDate

private val scheduleColors = listOf(LimeGreen, PurpleAccent, CardWhite, DarkSurface, CardGreen)

@Composable
fun ManageTasksScreen(
    viewModel: TaskViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.allTasks.collectAsState()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = LimeGreen,
                contentColor = DarkSurface,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkSurface
                        )
                    }
                    Text(
                        text = "Task schedule",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSurface,
                        letterSpacing = (-0.25).sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(DarkSurface)
                        .padding(horizontal = Spacing.sm, vertical = Spacing.xs)
                ) {
                    Icon(
                        Icons.Outlined.CalendarMonth,
                        contentDescription = "Toggle calendar",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(Spacing.xxs))
                    Text(
                        text = "Calendar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            // Week Calendar
            WeekCalendarRow(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            // Timeline
            val hours = listOf("08 AM", "09 AM", "10 AM", "11 AM", "12 PM", "01 PM", "02 PM", "03 PM", "04 PM", "05 PM")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg)
            ) {
                hours.forEachIndexed { hourIndex, hour ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(76.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Time label
                        Text(
                            text = hour,
                            fontSize = 11.sp,
                            color = MutedText,
                            modifier = Modifier
                                .width(52.dp)
                                .padding(end = Spacing.sm)
                        )

                        // Task block slot with staggered animation
                        if (hourIndex < tasks.size) {
                            val task = tasks[hourIndex]
                            val color = scheduleColors[hourIndex % scheduleColors.size]
                            val textColor = when (color) {
                                CardWhite -> DarkSurface
                                else -> Color.White
                            }

                            val visible = remember {
                                MutableTransitionState(false).apply { targetState = true }
                            }

                            AnimatedVisibility(
                                visibleState = visible,
                                enter = fadeIn(tween(250, delayMillis = hourIndex * 60)) +
                                        slideInHorizontally(
                                            initialOffsetX = { -40 },
                                            animationSpec = tween(250, delayMillis = hourIndex * 60)
                                        ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = Spacing.md)
                            ) {
                                ScheduleTaskBlock(
                                    title = task.title,
                                    timeLabel = if (task.dueTime.isNotEmpty()) task.dueTime else hour,
                                    count = (hourIndex % 4) + 1,
                                    blockColor = color,
                                    textColor = textColor,
                                    widthFraction = if (hourIndex % 3 == 0) 1f else 0.85f
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(Color(0xFFE8E8E8))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
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
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun ManageTasksScreenHeaderPreview() {
    TaskmanagerTheme {
        Text("Task schedule", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}
