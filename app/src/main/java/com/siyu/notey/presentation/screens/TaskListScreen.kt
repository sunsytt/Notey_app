
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus

@Composable
fun TaskListScreen(
    tasks: List<Task>,
    onChangeStatus: (Int, TaskStatus) -> Unit,
    onArchiveTask: (Int) -> Unit,
    onRegisterSession: (Int) -> Unit,
    onOpenTask: (Int) -> Unit
) {
    val visibleTasks = tasks.filter { !it.isArchived }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Mis tareas",
                style = MaterialTheme.typography.headlineMedium
            )

            if (visibleTasks.isEmpty()) {
                Text(
                    text = "Aún no tienes tareas. ¡Crea tu primera tarea!"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = visibleTasks,
                        key = { it.id }
                    ) { task ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
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
                                    "Estimación: ${
                                        task.estimatedMinutes?.let { "$it min" }
                                            ?: "Sin estimación"
                                    }"
                                )

                                TextButton(
                                    onClick = { onOpenTask(task.id) }
                                ) {
                                    Text("Ver detalles")
                                }

                                Text("Cambiar estado")

                                Column {
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
                                    horizontalArrangement =
                                        Arrangement.spacedBy(4.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            onRegisterSession(task.id)
                                        }
                                    ) {
                                        Text("+25 min")
                                    }

                                    TextButton(
                                        onClick = {
                                            onArchiveTask(task.id)
                                        }
                                    ) {
                                        Text("Archivar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun TaskStatus.toSpanish(): String =
    when (this) {
        TaskStatus.PENDING -> "Pendiente"
        TaskStatus.IN_PROGRESS -> "En progreso"
        TaskStatus.COMPLETED -> "Completada"
    }
