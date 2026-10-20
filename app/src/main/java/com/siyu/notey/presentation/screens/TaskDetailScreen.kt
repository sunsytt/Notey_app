
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.model.Task

@Composable
fun TaskDetailScreen(
    task: Task,
    activityRecords: List<ActivityRecord>,
    onRegisterSession: (Int) -> Unit,
    onBack: () -> Unit
) {
    val taskRecords = activityRecords.filter { it.taskId == task.id }
    val totalMinutes = taskRecords.sumOf { it.durationMinutes }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("← Volver")
            }

            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = task.description.ifBlank {
                    "Sin descripción"
                }
            )

            Text("Estado: ${task.status}")
            Text("Prioridad: ${task.priority}")
            Text("Categoría: ${task.category}")
            Text("Sesiones registradas: ${taskRecords.size}")
            Text("Tiempo registrado: $totalMinutes min")

            Text(
                "Tiempo estimado: ${
                    task.estimatedMinutes?.let { "$it min" }
                        ?: "Sin estimación"
                }"
            )

            Button(
                onClick = { onRegisterSession(task.id) }
            ) {
                Text("Registrar 25 minutos")
            }
        }
    }
}
