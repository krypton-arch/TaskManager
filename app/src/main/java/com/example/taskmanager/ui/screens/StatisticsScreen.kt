package com.example.taskmanager.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.example.taskmanager.data.Task
import com.example.taskmanager.ui.theme.CardBlue
import com.example.taskmanager.ui.theme.CardYellow
import com.example.taskmanager.ui.theme.ChipBackground
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.LimeGreen
import com.example.taskmanager.ui.theme.MutedText
import com.example.taskmanager.ui.theme.Spacing
import com.example.taskmanager.ui.theme.TaskmanagerTheme
import com.example.taskmanager.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun StatisticsScreen(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.allTasks.collectAsState()
    var selectedPeriod by remember { mutableStateOf("Weekly") }

    val stats by remember(selectedPeriod, tasks) {
        derivedStateOf { computeStats(tasks, selectedPeriod) }
    }

    val animatedCompleted by animateIntAsState(
        targetValue = stats.completedTasks,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "completed"
    )
    val animatedRate by animateIntAsState(
        targetValue = stats.completionRate,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "rate"
    )
    val animatedTotal by animateIntAsState(
        targetValue = stats.totalTasks,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "total"
    )

    LazyColumn(
        contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        modifier = modifier.fillMaxSize()
    ) {
        // Hero greeting
        item {
            Text(
                text = "Hello 👋\nyour overall\nscore is above\naverage",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkSurface,
                lineHeight = 44.sp,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(Spacing.md))
        }

        // Period toggle
        item {
            PeriodToggle(
                selected = selectedPeriod,
                onSelect = { selectedPeriod = it }
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
        }

        // Yellow Growth Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardYellow)
                    .padding(Spacing.lg)
            ) {
                Column {
                    Text(
                        text = "Growth",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MutedText
                    )
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "${if (stats.growthPercent >= 0) "+" else ""}${stats.growthPercent}%",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DarkSurface
                            )
                            Spacer(modifier = Modifier.height(Spacing.xxs))
                            Text(
                                text = "vs last ${selectedPeriod.lowercase()}",
                                fontSize = 12.sp,
                                color = MutedText
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Best Result",
                                fontSize = 12.sp,
                                color = MutedText
                            )
                            Text(
                                text = "${stats.bestDayCount} tasks",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkSurface
                            )
                        }
                    }
                }
            }
        }

        // Blue Progress Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardBlue)
                    .padding(Spacing.lg)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Your progress",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSurface
                        )
                        Spacer(modifier = Modifier.height(Spacing.xxs))
                        Text(
                            text = "You are doing well 😊",
                            fontSize = 13.sp,
                            color = MutedText
                        )
                    }
                    Text(
                        text = "${animatedRate}%",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkSurface
                    )
                }
            }
        }

        // Summary stats row
        item {
            Spacer(modifier = Modifier.height(Spacing.xs))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatMiniCard("Total", animatedTotal.toString(), LimeGreen, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(Spacing.sm))
                StatMiniCard("Completed", animatedCompleted.toString(), CardYellow, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(Spacing.sm))
                StatMiniCard("Pending", (animatedTotal - animatedCompleted).toString(), CardBlue, Modifier.weight(1f))
            }
        }

        item { Spacer(modifier = Modifier.height(Spacing.md)) }
    }
}

@Composable
private fun PeriodToggle(
    selected: String,
    onSelect: (String) -> Unit
) {
    val periods = listOf("Weekly", "Monthly")
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        periods.forEach { period ->
            val isSelected = period == selected
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) DarkSurface else ChipBackground,
                animationSpec = tween(200),
                label = "toggleBg"
            )
            val textColor = if (isSelected) Color.White else MutedText

            Text(
                text = period,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(bgColor)
                    .clickable { onSelect(period) }
                    .padding(horizontal = Spacing.md, vertical = Spacing.xs)
            )
        }
    }
}

@Composable
private fun StatMiniCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color)
            .padding(horizontal = Spacing.md, vertical = Spacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkSurface
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MutedText
            )
        }
    }
}

private data class Stats(
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val completionRate: Int,
    val growthPercent: Int,
    val bestDayCount: Int
)

private fun computeStats(tasks: List<Task>, period: String): Stats {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    val completedTasks = tasks.count { task ->
        try {
            val dueDate = LocalDate.parse(task.dueDate, formatter)
            dueDate.isBefore(today)
        } catch (e: Exception) {
            task.status == "Done"
        }
    }

    val total = tasks.size
    val pending = total - completedTasks
    val rate = if (total > 0) ((completedTasks.toFloat() / total) * 100).roundToInt() else 0

    // Growth: compare current period vs previous period
    val weekField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()
    val currentWeek = today.get(weekField)
    val currentMonth = today.monthValue

    val thisCount: Int
    val lastCount: Int

    if (period == "Weekly") {
        thisCount = tasks.count {
            try {
                val d = LocalDate.parse(it.dueDate, formatter)
                d.get(weekField) == currentWeek && d.year == today.year
            } catch (e: Exception) { false }
        }
        lastCount = tasks.count {
            try {
                val d = LocalDate.parse(it.dueDate, formatter)
                d.get(weekField) == currentWeek - 1 && d.year == today.year
            } catch (e: Exception) { false }
        }
    } else {
        thisCount = tasks.count {
            try {
                val d = LocalDate.parse(it.dueDate, formatter)
                d.monthValue == currentMonth && d.year == today.year
            } catch (e: Exception) { false }
        }
        lastCount = tasks.count {
            try {
                val d = LocalDate.parse(it.dueDate, formatter)
                d.monthValue == currentMonth - 1 && d.year == today.year
            } catch (e: Exception) { false }
        }
    }

    val growth = if (lastCount > 0) (((thisCount - lastCount).toFloat() / lastCount) * 100).roundToInt()
    else if (thisCount > 0) 100 else 0

    // Best day count
    val dayGroups = tasks.groupBy { it.dueDate }
    val bestDay = dayGroups.maxByOrNull { it.value.size }?.value?.size ?: 0

    return Stats(total, completedTasks, pending, rate, growth, bestDay)
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun StatMiniCardPreview() {
    TaskmanagerTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            StatMiniCard("Total", "12", LimeGreen, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            StatMiniCard("Done", "8", CardYellow, Modifier.weight(1f))
        }
    }
}
