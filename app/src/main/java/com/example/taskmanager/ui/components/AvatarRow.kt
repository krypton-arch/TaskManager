package com.example.taskmanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmanager.ui.theme.CardGreen
import com.example.taskmanager.ui.theme.CardYellow
import com.example.taskmanager.ui.theme.ChipBackground
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.PurpleAccent

private val avatarColors = listOf(PurpleAccent, CardGreen, CardYellow)

@Composable
fun AvatarRow(
    count: Int = 3,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        val displayCount = minOf(count, 3)
        for (i in 0 until displayCount) {
            Box(
                modifier = Modifier
                    .offset(x = (-8 * i).dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(avatarColors[i % avatarColors.size])
                    .border(2.dp, Color.White, CircleShape)
            )
        }

        Box(
            modifier = Modifier
                .offset(x = (-8 * displayCount).dp)
                .size(28.dp)
                .clip(CircleShape)
                .background(ChipBackground)
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add member",
                tint = DarkSurface,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
