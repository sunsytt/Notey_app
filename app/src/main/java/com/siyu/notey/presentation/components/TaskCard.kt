package com.siyu.notey.presentation.components


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
        modifier = Modifier.fillMaxWidth()
    ) {
        Row( Modifier
            .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium)
                Text(task.category.name, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PriorityChip(task.priority)
                    TaskStatusChip(task.status)
                }
                task.dueDate?.let {
                    Text("Vence: $it", style = MaterialTheme.typography.bodySmall)
                }
                val time = task.estimatedMinutes
                    ?.let {"$loggedMinutes/$it min"}
                    ?: "$loggedMinutes min sin registrar"
                    Text(time,
                        style = MaterialTheme.typography.bodySmall)
                }
            }
            if (menuActions.isNotEmpty()) {
                Box {
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
