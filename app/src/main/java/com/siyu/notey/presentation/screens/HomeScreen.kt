
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.presentation.state.TaskUiState

@Composable
fun HomeScreen(
    uiState: TaskUiState
) {
    val activeTasks = uiState.tasks.filter { task ->
        !task.isArchived && task.status != TaskStatus.COMPLETED
    }

    val completedTasks = uiState.tasks.filter { task ->
        !task.isArchived && task.status == TaskStatus.COMPLETED
    }

    val totalMinutes = uiState.activityRecords.sumOf { record ->
        record.durationMinutes
    }

    val completedIds: Set<Int> = completedTasks.map { task ->
        task.id
    }.toSet()

    val actualMinutes = uiState.activityRecords
        .filter { record -> record.taskId in completedIds }
        .sumOf { record -> record.durationMinutes }

    val estimatedMinutes = completedTasks
        .mapNotNull { task -> task.estimatedMinutes }
        .sum()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Notey",
                style = MaterialTheme.typography.headlineLarge
            )

            Text("Organiza tus tareas y registra tus avances.")

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Resumen",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text("Tareas activas: ${activeTasks.size}")
                    Text("Tareas completadas: ${completedTasks.size}")
                    Text("Tiempo registrado: $totalMinutes min")
                    Text("Tiempo en tareas completadas: $actualMinutes min")
                    Text(
                        "Tiempo estimado de tareas completadas: " +
                                "$estimatedMinutes min"
                    )
                }
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
