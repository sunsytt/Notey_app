
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siyu.notey.data.model.Category
import com.siyu.notey.data.model.Priority
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.presentation.state.TaskUiState
import com.siyu.notey.presentation.viewmodel.TaskViewModel

@Composable
fun TaskScreen(
    uiState: TaskUiState,
    onCreateTask: (
        String,
        String,
        Category,
        Priority,
        Int?
    ) -> Unit,
    onChangeStatus: (Int, TaskStatus) -> Unit
)
 {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Category.STUDY) }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var estimated by remember { mutableStateOf("30") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Notey", style = MaterialTheme.typography.headlineLarge)
            Text("Organiza tus tareas y registra tus avances.")

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
                Text(message, color = MaterialTheme.colorScheme.error)
            }

            Text("Mis tareas", style = MaterialTheme.typography.titleLarge)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.tasks, key = { it.id }) { task ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(task.title, style = MaterialTheme.typography.titleMedium)
                            if (task.description.isNotBlank()) {
                                Text(task.description)
                            }
                            Text("Estado: ${task.status}")
                            Text("Prioridad: ${task.priority}")
                            Text("Categoría: ${task.category}")
                            Text(
                                "Estimación: ${
                                    task.estimatedMinutes?.let { "$it min" } ?: "Sin estimación"
                                }"
                            )
                        }
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
