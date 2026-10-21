package com.siyu.notey.presentation.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Task

data class MenuAction(
    val label: String,
    val onClick:() -> Unit
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    loggedMinutes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    menuActions: List<MenuAction> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 12.dp
        )
    ) {
        Box {
            Column(
                Modifier.padding(
                    start = 16.dp, top = 16.dp, bottom = 16.dp,
                    end = if (menuActions.isNotEmpty()) 48.dp else 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                Text(task.category.name, style = MaterialTheme.typography.bodySmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PriorityChip(task.priority)
                    TaskStatusChip(task.status)
                }
                task.dueDate?.let {
                    Text("Vence: $it", style = MaterialTheme.typography.bodySmall)
                }
                val time = task.estimatedMinutes
                    ?.let { "$loggedMinutes / $it min" }
                    ?: "$loggedMinutes min · Sin estimación"
                Text(time, style = MaterialTheme.typography.bodySmall)
            }

            if (menuActions.isNotEmpty()) {
                Box(Modifier.align(Alignment.TopEnd)) {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        menuActions.forEach { action ->
                            DropdownMenuItem(
                                text = { Text(action.label) },
                                onClick = { expanded = false; action.onClick() }
                            )
                        }
                    }
                }
            }
        }
    }
}