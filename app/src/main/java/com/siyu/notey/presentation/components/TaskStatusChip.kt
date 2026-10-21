package com.siyu.notey.presentation.components

import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.siyu.notey.data.model.TaskStatus

@Composable
fun TaskStatusChip(status: TaskStatus) {
    val label = when (status) {
        TaskStatus.PENDING -> "Pendiente"
        TaskStatus.IN_PROGRESS -> "En progreso"
        TaskStatus.COMPLETED -> "Completada"
    }
    AssistChip(onClick = {}, label = { Text(label) })
}
