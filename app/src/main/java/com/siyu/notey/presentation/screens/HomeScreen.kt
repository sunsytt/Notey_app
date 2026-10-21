
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.siyu.notey.presentation.components.EmptyState
import com.siyu.notey.presentation.components.StatCard
import com.siyu.notey.presentation.components.TaskCard
import com.siyu.notey.presentation.state.TaskUiState

@Composable
fun HomeScreen(
    state: TaskUiState,
    onTaskClick: (Int) -> Unit,
    onNewTask: () -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0),
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 88.dp),   // espacio para el botón
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(Modifier.padding(top = 12.dp)) {
                Text("Hola", style = MaterialTheme.typography.bodyLarge)
                Text("Mi espacio", style = MaterialTheme.typography.headlineMedium)
            }

            // Porcentaje de finalización
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { state.completionPercent / 100f },
                            modifier = Modifier.size(72.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surface,
                            strokeWidth = 8.dp
                        )
                        Text("${state.completionPercent}%", style = MaterialTheme.typography.titleMedium)
                    }
                    Column {
                        Text("Finalización", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "${state.completedCount} de ${state.activeTasks.size} tareas completadas",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Resumen por estado
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("${state.pendingCount}", "Pendientes", Modifier.weight(1f),
                    MaterialTheme.colorScheme.tertiary)
                StatCard("${state.inProgressCount}", "En progreso", Modifier.weight(1f),
                    MaterialTheme.colorScheme.secondary)
                StatCard("${state.completedCount}", "Completadas", Modifier.weight(1f))
            }

            Text("Para hoy", style = MaterialTheme.typography.titleMedium)

            if (state.upcomingTasks.isEmpty()) {
                EmptyState(
                    title = "Todo al día",
                    message = "No tienes tareas pendientes. Crea una nueva cuando la necesites."
                )
            } else {
                state.upcomingTasks.forEach { task ->
                    TaskCard(
                        task = task,
                        loggedMinutes = state.minutesOf(task.id),
                        onClick = { onTaskClick(task.id) }
                    )
                }
            }
        }
    }
}
