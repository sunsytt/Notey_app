
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.presentation.components.EmptyState
import com.siyu.notey.presentation.components.StatCard
import com.siyu.notey.presentation.state.TaskUiState

@Composable
fun StatisticsScreen(state: TaskUiState) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Progreso", style = MaterialTheme.typography.headlineMedium)

        // Conteos
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard("${state.activeTasks.size}", "Tareas activas", Modifier.weight(1f))
            StatCard("${state.completionPercent}%", "Finalización", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard("${state.pendingCount}", "Pendientes", Modifier.weight(1f))
            StatCard("${state.inProgressCount}", "En progreso", Modifier.weight(1f))
            StatCard("${state.completedCount}", "Completadas", Modifier.weight(1f))
        }

        // Tiempo total
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Tiempo registrado", style = MaterialTheme.typography.titleMedium)
                Text("${state.totalMinutes} min", style = MaterialTheme.typography.headlineMedium)
                LinearProgressIndicator(
                    progress = { state.completionPercent / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Avance de tareas completadas", style = MaterialTheme.typography.bodySmall)
            }
        }

        // Estimado vs real
        Text("Estimado vs. real", style = MaterialTheme.typography.titleMedium)

        if (state.tasksWithEstimate.isEmpty()) {
            EmptyState(
                title = "Sin estimaciones",
                message = "Agrega un tiempo estimado a tus tareas para compararlo con el real."
            )
        } else {
            val diff = state.realOfEstimated - state.estimatedTotal
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Estimado: ${state.estimatedTotal} min")
                    Text("Real: ${state.realOfEstimated} min")
                    Text(
                        when {
                            diff > 0 -> "+$diff min sobre lo estimado"
                            diff < 0 -> "${-diff} min por debajo de lo estimado"
                            else -> "Igual a lo estimado"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            state.tasksWithEstimate.forEach { task ->
                val estimated = task.estimatedMinutes ?: return@forEach
                val real = state.minutesOf(task.id)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            task.title,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text("$real / $estimated min", style = MaterialTheme.typography.bodySmall)
                    }
                    LinearProgressIndicator(
                        progress = { (real.toFloat() / estimated).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Text(
            "Las tareas archivadas no se incluyen en estos indicadores, pero conservan su historial.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}