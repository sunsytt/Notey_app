package com.siyu.notey.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.siyu.notey.presentation.viewmodel.TaskViewModel

@Composable
fun AppNav(vm: TaskViewModel) {
    val nav = rememberNavController()
    val state by vm.uiState.collectAsStateWithLifecycle()
    val route = nav.currentBackStackEntryAsState().value?.destination?.route
    val showBar = bottomItems.any { it.route == route }

    Scaffold(
        bottomBar = {
            if (showBar) NavigationBar {
                bottomItems.forEach { s ->
                    NavigationBarItem(
                        selected = route == s.route,
                        onClick = { nav.navigate(s.route) {
                            popUpTo(Screen.Home.route); launchSingleTop = true } },
                        icon = { Icon(s.icon!!, null) },
                        label = { Text(s.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(nav, Screen.Home.route,
            Modifier.padding(padding)) {
            composable(Screen.Home.route) { /* DashboardScreen(state, ...) */ }
            composable(Screen.Tasks.route) { /* TaskListScreen(...) */ }
            composable(Screen.Stats.route) { /* StatisticsScreen(state) */ }
            composable(Screen.History.route) { /* HistoryScreen(state) */ }
            composable(Screen.Detail.route,
                arguments = listOf(navArgument("taskId") {
                    type = NavType.IntType
                })
            ) { entry ->
                val id = entry.arguments!!.getInt("taskId")
                val task = state.tasks.find { it.id == id }
                /* TaskDetailScreen(task, state, ...) */
            }
        }
    }
}