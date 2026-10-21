package com.siyu.notey.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.ui.theme.InkDark
import com.siyu.notey.ui.theme.MintSoft
import com.siyu.notey.ui.theme.PinkSoft
import com.siyu.notey.ui.theme.VioletSoft

@Composable
fun TaskStatusChip(status: TaskStatus) {
    val (label, color) = when (status) {
        TaskStatus.PENDING -> "Pendiente" to VioletSoft
        TaskStatus.IN_PROGRESS -> "En progreso" to PinkSoft
        TaskStatus.COMPLETED -> "Completada" to MintSoft
    }
    Surface(shape = RoundedCornerShape(50), color = color) {
        Text(
            label,
            color = InkDark,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
