package com.siyu.notey.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Priority
import com.siyu.notey.ui.theme.Coral
import com.siyu.notey.ui.theme.InkDark
import com.siyu.notey.ui.theme.Sky
import com.siyu.notey.ui.theme.Sunny

private fun Priority.label() = when (this) {
    Priority.HIGH -> "Alta"
    Priority.MEDIUM -> "Media"
    Priority.LOW -> "Baja"
}

private fun Priority.color() = when (this) {
    Priority.HIGH -> Coral
    Priority.MEDIUM -> Sunny
    Priority.LOW -> Sky
}

// Solo lectura: se usa dentro de las tarjetas
@Composable
fun PriorityChip(priority: Priority) {
    Surface(shape = RoundedCornerShape(50), color = priority.color()) {
        Text(
            text = priority.label(),
            color = InkDark,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

// Para elegir: se usa en el formulario
@Composable
fun PrioritySelector(
    selected: Priority,
    onSelect: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Priority.entries.forEach { priority ->
            FilterChip(
                selected = priority == selected,
                onClick = { onSelect(priority) },
                label = { Text(priority.label()) }
            )
        }
    }
}