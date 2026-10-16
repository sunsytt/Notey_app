
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Category
import com.siyu.notey.data.model.Priority
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.presentation.state.TaskUiState

@Composable
fun TaskScreen(
    uiState: TaskUiState,
    onCreateTask: (String, String, Category, Priority, Int?) -> Unit,
    onChangeStatus: (Int, TaskStatus) -> Unit,
    onArchiveTask: (Int) -> Unit,
    onRegisterSession: (Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Category.STUDY) }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var estimated by remember { mutableStateOf("30") }

    val activeTasks = uiState.tasks.filter { task ->
        !task.isArchived && task.status != TaskStatus.COMPLETED
    }

    val completedTasks = uiState.tasks.filter { task ->
        !task.isArchived && task.status == TaskStatus.COMPLETED
    }

    val totalMinutes: Int = uiState.activityRecords.sumOf { record ->
        record.durationMinutes
    }

    val completedIds: Set<String> = completedTasks
        .map { task -> task.id.toString() }
        .toSet()

    val actualMinutes: Int = uiState.activityRecords
        .filter { record -> record.taskId.toString() in completedIds }
        .sumOf { record -> record.durationMinutes }

    val estimatedMinutes: Int = completedTasks
        .mapNotNull { task -> task.estimatedMinutes }
        .sum()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "Notey",
                style = MaterialTheme.typography.headlineLarge
            )

            Text("Organiza tus tareas y registra tus avances.")

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Resumen",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text("Tareas activas: ${activeTasks.size}")
                    Text("Tareas completadas: ${completedTasks.size}")
                    Text("Tiempo registrado: $totalMinutes min")
                    Text("Tiempo en tareas completadas: $actualMinutes min")
                    Text("Tiempo estimado de tareas completadas: $estimatedMinutes min")
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Categoría")

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Category.entries.forEach { option ->
                    TextButton(onClick = { category = option }) {
                        Text(
                            if (category == option) "✓ ${option.name}"
                            else option.name
                        )
                    }
                }
            }

            Text("Prioridad")

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Priority.entries.forEach { option ->
                    TextButton(onClick = { priority = option }) {
                        Text(
                            if (priority == option) "✓ ${option.name}"
                            else option.name
                        )
                    }
                }
            }

            OutlinedTextField(
                value = estimated,
                onValueChange = { estimated = it },
                label = { Text("Tiempo estimado (minutos, opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    val minutes = estimated.toIntOrNull()
                    onCreateTask(
                        title,
                        description,
                        category,
                        priority,
                        if (estimated.isBlank()) null else minutes
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear tarea")
            }

            uiState.errorMessage?.let { message ->
                Text(
                    message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Text(
                "Mis tareas",
                style = MaterialTheme.typography.titleLarge
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    uiState.tasks.filterNot { it.isArchived },
                    key = { it.id }
                ) { task ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                task.title,
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

                            Text("Cambiar estado")

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
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

                            TextButton(
                                onClick = { onRegisterSession(task.id) }
                            ) {
                                Text("+ Registrar 25 min")
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
