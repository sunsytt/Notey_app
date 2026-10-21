
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Category
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.presentation.components.CategorySelector
import com.siyu.notey.presentation.components.ConfirmDialog
import com.siyu.notey.presentation.components.EmptyState
import com.siyu.notey.presentation.components.MenuAction
import com.siyu.notey.presentation.components.TaskCard
import com.siyu.notey.presentation.state.TaskUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    state: TaskUiState,
    categories: List<Category>,
    onTaskClick: (Int) -> Unit,
    onNewTask: () -> Unit,
    onEditTask: (Int) -> Unit,
    onArchiveTask: (Int) -> Unit,
    onOpenArchived: () -> Unit,
    onSelectStatus: (TaskStatus?) -> Unit,
    onSelectCategory: (Category?) -> Unit
) {
    // Estado de la UI: qué tarea se va a archivar
    var taskToArchive by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0),   // AppNav ya aplica los márgenes
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewTask,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva tarea") }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mis tareas", style = MaterialTheme.typography.headlineMedium)
                TextButton(onClick = onOpenArchived) {
                    Text("Archivadas (${state.archivedTasks.size})")
                }
            }

            Spacer(Modifier.height(8.dp))

            StatusFilterRow(selected = state.selectedStatus, onSelect = onSelectStatus)

            Spacer(Modifier.height(8.dp))

            CategorySelector(
                categories = categories,
                selected = state.selectedCategory,
                // tocar otra vez la categoría activa quita el filtro
                onSelect = { onSelectCategory(if (it == state.selectedCategory) null else it) }
            )

            Spacer(Modifier.height(12.dp))

            if (state.filteredTasks.isEmpty()) {
                EmptyState(
                    title = "No hay tareas",
                    message = "Prueba otro filtro o crea una tarea nueva."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(state.filteredTasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            loggedMinutes = state.minutesOf(task.id),
                            onClick = { onTaskClick(task.id) },
                            menuActions = listOf(
                                MenuAction("Editar") { onEditTask(task.id) },
                                MenuAction("Archivar") { taskToArchive = task }
                            )
                        )
                    }
                }
            }
        }
    }

    taskToArchive?.let { task ->
        ConfirmDialog(
            title = "Archivar tarea",
            message = "“${task.title}” saldrá de la lista activa, pero conservará su historial. Podrás restaurarla después.",
            confirmText = "Archivar",
            onConfirm = { onArchiveTask(task.id); taskToArchive = null },
            onDismiss = { taskToArchive = null }
        )
    }
}

@Composable
private fun StatusFilterRow(
    selected: TaskStatus?,
    onSelect: (TaskStatus?) -> Unit
) {
    val options = listOf<Pair<String, TaskStatus?>>(
        "Todos" to null,
        "Pendientes" to TaskStatus.PENDING,
        "En progreso" to TaskStatus.IN_PROGRESS,
        "Completadas" to TaskStatus.COMPLETED
    )
    Row(
        Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { (label, status) ->
            FilterChip(
                selected = selected == status,
                onClick = { onSelect(status) },
                label = { Text(label) }
            )
        }
    }
}