package com.siyu.notey.presentation.components

@Composable
fun TaskStatusChip(status: TaskStatus) {
    val label = when (status) {
        TaskStatus.PENDING -> "Pendiente"
        TaskStatus.IN_PROGRESS -> "En progreso"
        TaskStatus.COMPLETED -> "Completada"
    }
    AssistChip(onClick = {}, label = { Text(label) })
}