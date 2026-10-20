
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
fun StatisticsScreen(
    uiState: TaskUiState
) {
    val activeTasks = uiState.tasks.filter {
        !it.isArchived && it.status != TaskStatus.COMPLETED
    }

    val completedTasks = uiState.tasks.filter {
        !it.isArchived && it.status == TaskStatus.COMPLETED
    }

    val totalMinutes = uiState.activityRecords.sumOf {
        it.durationMinutes
    }

    val completedIds = completedTasks.map { it.id }.toSet()

    val completedMinutes = uiState.activityRecords
        .filter { it.taskId in completedIds }
        .sumOf { it.durationMinutes }

    val estimatedMinutes = completedTasks
        .mapNotNull { it.estimatedMinutes }
        .sum()

    val archivedCount = uiState.tasks.count { it.isArchived }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Estadísticas",
                style = MaterialTheme.typography.headlineMedium
            )

            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Tareas activas: ${activeTasks.size}")
                    Text("Tareas completadas: ${completedTasks.size}")
                    Text("Tareas archivadas: $archivedCount")
                    Text("Tiempo total registrado: $totalMinutes min")
                    Text("Tiempo en tareas completadas: $completedMinutes min")
                    Text("Tiempo estimado completado: $estimatedMinutes min")
                }
            }
        }
    }
}
