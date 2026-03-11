package com.example.taskmanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.LimeGreen
import com.example.taskmanager.ui.theme.MutedText
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
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0..6) {
            val date = startOfWeek.plusDays(i.toLong())
            val isSelected = date == selectedDate
            val isToday = date == today

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onDateSelected(date) }
                    .padding(vertical = 4.dp)
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
                        .padding(top = 6.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) LimeGreen else Color.Transparent
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) DarkSurface else DarkSurface.copy(alpha = 0.7f)
                    )
                }

                if (isToday) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(DarkSurface)
                    )
                }
            }
        }
    }
}
