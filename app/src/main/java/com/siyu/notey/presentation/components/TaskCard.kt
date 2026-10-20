package com.siyu.notey.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus

@Composable
fun TaskCard(
    task: Task,
    onChangeStatus: (Int, TaskStatus) -> Unit,
    onRegisterSession: (Int) -> Unit,
    onArchiveTask: (Int) -> Unit,
    onOpenDetails: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium
            )

            if (task.description.isNotBlank()) {
                Text(task.description)
            }

            Text("Estado: ${task.status.toSpanish()}")
            Text("Prioridad: ${task.priority}")
            Text("Categoría: ${task.category}")

            Text(
                text = "Estimación: ${
                    task.estimatedMinutes?.let { "$it min" }
                        ?: "Sin estimación"
                }"
            )

            Text(
                text = "Cambiar estado",
                style = MaterialTheme.typography.labelLarge
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TaskStatus.entries.forEach { option ->
                    TextButton(
                        onClick = {
                            onChangeStatus(task.id, option)
                        }
                    ) {
                        Text(
                            if (task.status == option) {
                                "✓ ${option.toSpanish()}"
                            } else {
                                option.toSpanish()
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { onRegisterSession(task.id) }
                ) {
                    Text("+ 25 min")
                }

                TextButton(
                    onClick = { onOpenDetails(task.id) }
                ) {
                    Text("Detalles")
                }

                TextButton(
                    onClick = { onArchiveTask(task.id) }
                ) {
                    Text("Archivar")
                }
            }
        }
    }
}

private fun TaskStatus.toSpanish(): String {
    return when (this) {
        TaskStatus.PENDING -> "Pendiente"
        TaskStatus.IN_PROGRESS -> "En progreso"
        TaskStatus.COMPLETED -> "Completada"
    }
}