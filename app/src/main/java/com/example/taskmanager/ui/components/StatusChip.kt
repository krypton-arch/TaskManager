package com.example.taskmanager.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.taskmanager.ui.theme.ChipBackground
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.LimeGreen
import com.example.taskmanager.ui.theme.MutedText
import com.example.taskmanager.ui.theme.Spacing
import com.example.taskmanager.ui.theme.TaskmanagerTheme

@Composable
fun StatusChip(
    label: String,
    count: Int,
    chipColor: Color,
    isOutlined: Boolean = false,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(50)

    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) chipColor else if (isOutlined) Color.Transparent else chipColor,
        animationSpec = tween(200),
        label = "chipColor"
    )

    val bgModifier = if (isOutlined && !isSelected) {
        Modifier
            .border(1.dp, MutedText, shape)
            .clip(shape)
    } else {
        Modifier
            .clip(shape)
            .background(animatedColor)
    }

    Row(
        modifier = modifier
            .sizeIn(minHeight = 36.dp)
            .then(bgModifier)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = Spacing.md, vertical = Spacing.xs)
            .semantics { contentDescription = "$label: $count tasks" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isOutlined && !isSelected) MutedText else DarkSurface
        )
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(DarkSurface),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun StatusChipPreview() {
    TaskmanagerTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusChip("In progress", 3, LimeGreen)
            StatusChip("On hold", 1, ChipBackground, isOutlined = true)
        }
    }
}
