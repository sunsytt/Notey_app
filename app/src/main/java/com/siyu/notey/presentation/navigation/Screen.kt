package com.siyu.notey.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector


//sealed class Screen + bottomItems, define destinos
sealed class Screen(val route: String, val label: String = "", val icon: ImageVector? = null) {
    data object Home : Screen("home", "Inicio", Icons.Default.Home)
    data object Tasks : Screen("tasks", "Tareas", Icons.AutoMirrored.Filled.List)
    data object Stats : Screen("stats", "Progreso", Icons.Default.Info)
    data object History : Screen("history", "Historial", Icons.Default.DateRange)
    data object Form : Screen("form?taskId={taskId}")
    data object Detail : Screen("detail/{taskId}")
    data object Archived : Screen("archived")
}
val bottomItems = listOf(Screen.Home, Screen.Tasks, Screen.Stats, Screen.History)