package com.siyu.notey.presentation.components

@Composable
fun PrioritySelector(priority: Priority) {
    val (label, color) = when (priority) {
        Priority.HIGH -> "Alta" to TomatoJam
        Priority.MEDIUM -> "Media" to SoftPeach
        Priority.LOW -> "Baja" to SkyBlue
    }
    val onColor = if (priority == Priority.HIGH) Color.White else InkDark
    Surface(shape = RoundedCornerShape(50), color = color) {
        Text(label, color = onColor, style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}