package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.presentation.components.EmptyState
import com.siyu.notey.presentation.components.SessionItem
import androidx.compose.foundation.lazy.items

@Composable
fun HistoryScreen(
    tasks: List<Task>,
    records: List<ActivityRecord>
) {
    // Para buscar la tarea de cada sesión sin recorrer la lista cada vez
    val tasksById = remember(tasks) { tasks.associateBy { it.id } }
    val sorted = remember(records) { records.sortedByDescending { it.date } }

    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Historial de actividad",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        if (sorted.isEmpty()) {
            EmptyState(
                title = "Sin actividad",
                message = "Las sesiones que registres en tus tareas aparecerán aquí."
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(sorted, key = { it.id }) { record ->
                    val task = tasksById[record.taskId]
                    SessionItem(
                        record = record,
                        taskTitle = task?.title ?: "Tarea no disponible",
                        badge = when {
                            task == null -> null
                            task.isArchived -> "Archivada"
                            task.status == TaskStatus.COMPLETED -> "Completada"
                            else -> null
                        }
                    )
                }
            }
        }
    }
}

