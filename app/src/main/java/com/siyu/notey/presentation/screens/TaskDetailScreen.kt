
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.presentation.components.EmptyState
import com.siyu.notey.presentation.components.PriorityChip
import com.siyu.notey.presentation.components.SessionForm
import com.siyu.notey.presentation.components.SessionItem
import com.siyu.notey.presentation.components.TaskStatusChip
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.siyu.notey.presentation.components.ConfirmDialog
@Composable
fun TaskDetailScreen(
    task: Task?,
    records: List<ActivityRecord>,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
    onChangeStatus: (Int, TaskStatus) -> Unit,
    onArchive: (Int) -> Unit,
    onRestore: (Int) -> Unit,
    onRegisterSession: (taskId: Int, date: String, minutes: Int, note: String?) -> Unit
) {
    // La tarea puede no existir (id inválido)
    if (task == null) {
        Column {
            TextButton(onClick = onBack) { Text("← Volver") }
            EmptyState("Tarea no encontrada", "Es posible que haya sido eliminada.")
        }
        return
    }

    // Estado efímero de la UI: diálogos abiertos
    var showSessionForm by remember { mutableStateOf(false) }
    var showArchiveDialog by remember { mutableStateOf(false) }

    val totalMinutes = records.sumOf { it.durationMinutes }
    val sortedRecords = records.sortedByDescending { it.date }

    LazyColumn(
        Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(task.title, style = MaterialTheme.typography.headlineMedium)
            if (task.description.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(task.description, style = MaterialTheme.typography.bodyMedium)
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(task.category.name, style = MaterialTheme.typography.labelLarge)
                PriorityChip(task.priority)
                TaskStatusChip(task.status)
                if (task.isArchived) AssistChip(onClick = {}, label = { Text("Archivada") })
            }
            Text(
                "Fecha límite: ${task.dueDate ?: "Sin fecha"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item { TimeSummary(totalMinutes, task.estimatedMinutes) }

        if (!task.isArchived) {
            item {
                Text("Cambiar estado", style = MaterialTheme.typography.labelLarge)
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "Pendiente" to TaskStatus.PENDING,
                        "En progreso" to TaskStatus.IN_PROGRESS,
                        "Completada" to TaskStatus.COMPLETED
                    ).forEach { (label, status) ->
                        FilterChip(
                            selected = task.status == status,
                            onClick = { onChangeStatus(task.id, status) },
                            label = { Text(label) }
                        )
                    }
                }
            }
            item {
                Button(onClick = { showSessionForm = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Registrar sesión")
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { onEdit(task.id) }, modifier = Modifier.weight(1f)) {
                        Text("Editar")
                    }
                    OutlinedButton(onClick = { showArchiveDialog = true }, modifier = Modifier.weight(1f)) {
                        Text("Archivar")
                    }
                }
            }
        } else {
            item {
                Button(onClick = { onRestore(task.id); onBack() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Restaurar")
                }
            }
        }

        item { Text("Historial de sesiones", style = MaterialTheme.typography.titleMedium) }

        if (sortedRecords.isEmpty()) {
            item { EmptyState("Sin sesiones", "Registra tu primera sesión de trabajo.") }
        } else {
            items(sortedRecords, key = { it.id }) { SessionItem(it) }
        }
    }

    if (showSessionForm) {
        SessionForm(
            onConfirm = { date, minutes, note ->
                onRegisterSession(task.id, date, minutes, note)
                showSessionForm = false
            },
            onDismiss = { showSessionForm = false }
        )
    }

    if (showArchiveDialog) {
        ConfirmDialog(
            title = "Archivar tarea",
            message = "Saldrá de la lista activa, pero conservará su historial. Podrás restaurarla después.",
            confirmText = "Archivar",
            onConfirm = { onArchive(task.id); showArchiveDialog = false; onBack() },
            onDismiss = { showArchiveDialog = false }
        )
    }
}

@Composable
private fun TimeSummary(logged: Int, estimated: Int?) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Tiempo registrado: $logged min", style = MaterialTheme.typography.titleMedium)
            if (estimated == null) {
                Text("Sin estimación", style = MaterialTheme.typography.bodyMedium)
            } else {
                val diff = logged - estimated
                Text("Estimado: $estimated min", style = MaterialTheme.typography.bodyMedium)
                Text(
                    when {
                        diff > 0 -> "+$diff min sobre lo estimado"
                        diff < 0 -> "${-diff} min por debajo de lo estimado"
                        else -> "Igual a lo estimado"
                    },
                    style = MaterialTheme.typography.bodySmall
                )
                LinearProgressIndicator(
                    progress = { (logged.toFloat() / estimated).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
