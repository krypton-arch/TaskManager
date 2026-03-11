package com.example.taskmanager.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.taskmanager.ui.theme.CardBlue
import com.example.taskmanager.ui.theme.CardGreen
import com.example.taskmanager.ui.theme.CardYellow
import com.example.taskmanager.ui.theme.ChipBackground
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.LimeGreen
import com.example.taskmanager.ui.theme.MutedText
import com.example.taskmanager.ui.theme.PurpleAccent
import com.example.taskmanager.ui.theme.Spacing
import com.example.taskmanager.ui.theme.TaskmanagerTheme
import com.example.taskmanager.viewmodel.TaskViewModel
import com.example.taskmanager.viewmodel.UserPreferencesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private data class AvatarOption(val name: String, val color: Color)

private val avatarOptions = listOf(
    AvatarOption("Purple", PurpleAccent),
    AvatarOption("Green", CardGreen),
    AvatarOption("Blue", CardBlue),
    AvatarOption("Yellow", CardYellow),
    AvatarOption("Lime", LimeGreen)
)

@Composable
fun ProfileScreen(
    taskViewModel: TaskViewModel,
    prefsViewModel: UserPreferencesViewModel,
    modifier: Modifier = Modifier
) {
    val userName by prefsViewModel.userName.collectAsState()
    val avatarColorName by prefsViewModel.avatarColor.collectAsState()
    val themePreference by prefsViewModel.themePreference.collectAsState()
    val tasks by taskViewModel.allTasks.collectAsState()

    var isEditingName by remember { mutableStateOf(false) }
    var editedName by remember(userName) { mutableStateOf(userName) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val avatarColor = avatarOptions.find { it.name == avatarColorName }?.color ?: PurpleAccent
    val initials = userName.take(2).uppercase()

    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val completedCount = tasks.count { task ->
        try {
            val d = LocalDate.parse(task.dueDate, formatter)
            d.isBefore(today)
        } catch (e: Exception) {
            task.status == "Done"
        }
    }
    val pendingCount = tasks.size - completedCount

    LazyColumn(
        contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
        modifier = modifier.fillMaxSize()
    ) {
        // Avatar
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSurface
                        )
                    }
                    IconButton(
                        onClick = { isEditingName = true },
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(DarkSurface)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit profile",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.sm))

                if (isEditingName) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        singleLine = true,
                        modifier = Modifier.width(200.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurpleAccent,
                            cursorColor = DarkSurface
                        )
                    )
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        TextButton(onClick = {
                            editedName = userName
                            isEditingName = false
                        }) {
                            Text("Cancel", color = MutedText)
                        }
                        Button(
                            onClick = {
                                prefsViewModel.updateUserName(editedName.trim())
                                isEditingName = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurface,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = editedName.isNotBlank()
                        ) {
                            Text("Save")
                        }
                    }
                } else {
                    Text(
                        text = userName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSurface,
                        modifier = Modifier.clickable { isEditingName = true }
                    )
                }
            }
        }

        // Avatar Color Picker
        item {
            Text(
                text = "Avatar Color",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MutedText
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                avatarOptions.forEach { option ->
                    val isSelected = option.name == avatarColorName
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(option.color)
                            .then(
                                if (isSelected) Modifier.border(
                                    BorderStroke(2.dp, DarkSurface),
                                    CircleShape
                                ) else Modifier
                            )
                            .clickable { prefsViewModel.updateAvatarColor(option.name) }
                    )
                }
            }
        }

        // Theme Toggle
        item {
            Text(
                text = "Theme",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MutedText
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                listOf("Light", "Dark", "System").forEach { theme ->
                    val isSelected = theme == themePreference
                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) DarkSurface else ChipBackground,
                        animationSpec = tween(200),
                        label = "themeBg"
                    )
                    val textColor = if (isSelected) Color.White else MutedText

                    Text(
                        text = theme,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(bgColor)
                            .clickable { prefsViewModel.updateThemePreference(theme) }
                            .padding(horizontal = Spacing.md, vertical = Spacing.xs)
                    )
                }
            }
        }

        // Task Summary Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardGreen)
                    .padding(Spacing.lg)
            ) {
                Column {
                    Text(
                        text = "Task Summary",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSurface
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatColumn("Total", tasks.size.toString())
                        StatColumn("Done", completedCount.toString())
                        StatColumn("Pending", pendingCount.toString())
                    }
                }
            }
        }

        // Clear All Tasks
        item {
            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.6f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text(
                    text = "Clear All Tasks",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        item { Spacer(modifier = Modifier.height(Spacing.md)) }
    }

    // Confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Clear All Tasks?", fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently delete all your tasks. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        taskViewModel.deleteAllTasks()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = MutedText)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DarkSurface
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = MutedText
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun StatColumnPreview() {
    TaskmanagerTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            StatColumn("Total", "12")
            StatColumn("Done", "8")
            StatColumn("Pending", "4")
        }
    }
}
