package com.siyu.notey.presentation.navigation

import android.R
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.siyu.notey.data.local.SeedData
import com.siyu.notey.presentation.screens.ArchiveScreen
import com.siyu.notey.presentation.screens.HistoryScreen
import com.siyu.notey.presentation.screens.HomeScreen
import com.siyu.notey.presentation.screens.StatisticsScreen
import com.siyu.notey.presentation.screens.TaskDetailScreen
import com.siyu.notey.presentation.screens.TaskFormScreen
import com.siyu.notey.presentation.screens.TaskListScreen
import com.siyu.notey.presentation.viewmodel.TaskViewModel
import androidx.activity.SystemBarStyle
import com.siyu.notey.ui.theme.InkDark
import com.siyu.notey.ui.theme.TopBarCoral

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav(vm: TaskViewModel) {
    val nav = rememberNavController()
    val state by vm.uiState.collectAsStateWithLifecycle()
    val route = nav.currentBackStackEntryAsState().value?.destination?.route
    val showBar = bottomItems.any { it.route == route }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            vm.clearError()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("TaskFlow", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TopBarCoral,        // o TopBarTurquoise
                    titleContentColor = InkDark
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },

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
            composable(Screen.Home.route) {
                HomeScreen(
                    state = state,
                    onTaskClick = { nav.navigate("detail/$it") },
                    onNewTask = { nav.navigate("form") }
                )
            }
            composable(Screen.Tasks.route) {
                TaskListScreen(
                    state = state,
                    categories = SeedData.categories,
                    onTaskClick = { id -> nav.navigate("detail/$id") },
                    onNewTask = { nav.navigate("form") },
                    onEditTask = { id -> nav.navigate("form?taskId=$id") },
                    onArchiveTask = vm::archiveTask,
                    onOpenArchived = { nav.navigate(Screen.Archived.route) },
                    onSelectStatus = vm::selectStatus,
                    onSelectCategory = vm::selectCategory
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("taskId") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("taskId") ?: -1

                TaskDetailScreen(
                    task = state.tasks.find { it.id == id },
                    records = state.activityRecords.filter { it.taskId == id },
                    onBack = { nav.popBackStack() },
                    onEdit = { nav.navigate("form?taskId=$it") },
                    onChangeStatus = vm::updateTaskStatus,
                    onArchive = vm::archiveTask,
                    onRestore = vm::restoreTask,
                    onRegisterSession = vm::registerSession
                )
            }
            composable(Screen.Stats.route) {
                StatisticsScreen(state = state)
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    tasks = state.tasks,
                    records = state.activityRecords
                )
            }
            composable(
                route = "form?taskId={taskId}",
                arguments = listOf(navArgument("taskId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { entry ->
                val id = entry.arguments?.getInt("taskId") ?: -1
                val task = state.tasks.find { it.id == id }   // null si es una tarea nueva

                TaskFormScreen(
                    taskToEdit = task,
                    categories = SeedData.categories,
                    onSave = { title, desc, cat, prio, due, mins ->
                        if (task == null) vm.createTask(title, desc, cat, prio, due, mins)
                        else vm.updateTask(task.id, title, desc, cat, prio, due, mins)
                        nav.popBackStack()
                    },
                    onCancel = { nav.popBackStack() }
                )

            }
            composable(Screen.Archived.route) {
                ArchiveScreen(
                    archivedTasks = state.archivedTasks,
                    minutesOf = state::minutesOf,
                    onBack = { nav.popBackStack() },
                    onTaskClick = { nav.navigate("detail/$it") },
                    onRestore = vm::restoreTask
                )
            }
        }
    }
}
