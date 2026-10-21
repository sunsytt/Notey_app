package com.siyu.notey.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Priority
import com.siyu.notey.ui.theme.InkDark
import com.siyu.notey.ui.theme.SkyBlue
import com.siyu.notey.ui.theme.SoftPeach
import com.siyu.notey.ui.theme.TomatoJam

@Composable
fun PriorityChip(priority: Priority) {
    val (label, color) = when (priority) {
        Priority.HIGH -> "Alta" to TomatoJam
        Priority.MEDIUM -> "Media" to SoftPeach
        Priority.LOW -> "Baja" to SkyBlue
    }
    val onColor = if (priority == Priority.HIGH) Color.White else InkDark
    Surface(shape = RoundedCornerShape(50), color = color) {
        Text(label,
            color = onColor,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}
