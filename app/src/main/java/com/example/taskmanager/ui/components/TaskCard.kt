package com.example.taskmanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanager.data.Task
import com.example.taskmanager.ui.theme.CardGreen
import com.example.taskmanager.ui.theme.CardWhite
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.LimeGreen
import com.example.taskmanager.ui.theme.MutedText
import com.example.taskmanager.ui.theme.PurpleAccent

private val cardColors = listOf(PurpleAccent, CardWhite, CardGreen, CardWhite)

@Composable
fun TaskCard(
    task: Task,
    index: Int,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = cardColors[index % cardColors.size]
    val isLight = cardColor == CardWhite
    val textColor = if (isLight) DarkSurface else Color.White
    val subtextColor = if (isLight) MutedText else Color.White.copy(alpha = 0.8f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onEdit(task) },
                onLongClick = { onDelete(task) }
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = task.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = subtextColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = if (task.dueDate.isNotEmpty()) "${task.dueDate} ${task.dueTime}" else "No due date",
                    fontSize = 12.sp,
                    color = subtextColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Priority badge
                Text(
                    text = "${task.priority} Priority",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkSurface,
                    modifier = Modifier
                        .background(LimeGreen, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )

                Text(
                    text = "3 In team",
                    fontSize = 12.sp,
                    color = subtextColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            AvatarRow(count = 3)
        }
    }
}
