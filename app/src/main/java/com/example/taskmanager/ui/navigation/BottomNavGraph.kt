package com.example.taskmanager.ui.navigation

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.ui.components.AddEditTaskDialog
import com.example.taskmanager.ui.components.TopBar
import com.example.taskmanager.ui.screens.ManageTasksScreen
import com.example.taskmanager.ui.screens.ProfileScreen
import com.example.taskmanager.ui.screens.StatisticsScreen
import com.example.taskmanager.ui.screens.TaskListScreen
import com.example.taskmanager.ui.theme.DarkSurface
import com.example.taskmanager.ui.theme.MutedText
import com.example.taskmanager.ui.theme.OffWhiteBackground
import com.example.taskmanager.ui.theme.Spacing
import com.example.taskmanager.ui.theme.TaskmanagerTheme
import com.example.taskmanager.viewmodel.TaskViewModel
import com.example.taskmanager.viewmodel.UserPreferencesViewModel

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    data object Home : BottomNavItem("home", Icons.Default.GridView, "Home")
    data object Schedule : BottomNavItem("schedule", Icons.Default.BarChart, "Schedule")
    data object Stats : BottomNavItem("stats", Icons.Default.Settings, "Stats")
    data object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Schedule,
    BottomNavItem.Stats,
    BottomNavItem.Profile
)

@Composable
fun BottomNavGraph(
    viewModel: TaskViewModel,
    userPrefsViewModel: UserPreferencesViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = OffWhiteBackground,
        topBar = {
            TopBar(onAddClick = { showAddDialog = true })
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .height(72.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route == item.route
                    } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White.copy(alpha = 0.15f))
                                        .padding(horizontal = Spacing.md, vertical = Spacing.xs),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = MutedText,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(250, easing = FastOutSlowInEasing)) +
                        slideInHorizontally(
                            initialOffsetX = { it / 8 },
                            animationSpec = tween(250)
                        )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(200)) +
                        slideOutHorizontally(
                            targetOffsetX = { -it / 8 },
                            animationSpec = tween(200)
                        )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(250, easing = FastOutSlowInEasing)) +
                        slideInHorizontally(
                            initialOffsetX = { -it / 8 },
                            animationSpec = tween(250)
                        )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(200)) +
                        slideOutHorizontally(
                            targetOffsetX = { it / 8 },
                            animationSpec = tween(200)
                        )
            }
        ) {
            composable(BottomNavItem.Home.route) {
                TaskListScreen(viewModel = viewModel)
            }
            composable(BottomNavItem.Schedule.route) {
                ManageTasksScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(BottomNavItem.Stats.route) {
                StatisticsScreen(viewModel = viewModel)
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    taskViewModel = viewModel,
                    prefsViewModel = userPrefsViewModel
                )
            }
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
private fun PlaceholderPreview() {
    TaskmanagerTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Preview", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkSurface)
        }
    }
}
