package com.example.taskmanager.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.LimeGreen
import com.example.taskmanager.ui.theme.MutedText
import com.example.taskmanager.ui.theme.Spacing
import com.example.taskmanager.ui.theme.TaskmanagerTheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekCalendarRow(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val startOfWeek = selectedDate.minusDays(selectedDate.dayOfWeek.value.toLong() % 7)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0..6) {
            val date = startOfWeek.plusDays(i.toLong())
            val isSelected = date == selectedDate
            val isToday = date == today

            val bgColor by animateColorAsState(
                targetValue = if (isSelected) LimeGreen else Color.Transparent,
                animationSpec = tween(180),
                label = "calBg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) DarkSurface else MutedText,
                animationSpec = tween(180),
                label = "calText"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .sizeIn(minWidth = 44.dp, minHeight = 56.dp)
                    .clickable { onDateSelected(date) }
                    .padding(vertical = Spacing.xxs)
                    .semantics {
                        contentDescription = "${date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${date.dayOfMonth}"
                    }
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        .first().toString(),
                    fontSize = 11.sp,
                    color = MutedText,
                    fontWeight = FontWeight.Medium
                )

                Box(
                    modifier = Modifier
                        .padding(top = Spacing.xxs)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(bgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }

                if (isToday) {
                    Box(
                        modifier = Modifier
                            .padding(top = Spacing.xxs)
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(DarkSurface)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun WeekCalendarRowPreview() {
    TaskmanagerTheme {
        WeekCalendarRow(
            selectedDate = LocalDate.now(),
            onDateSelected = {}
        )
    }
}
