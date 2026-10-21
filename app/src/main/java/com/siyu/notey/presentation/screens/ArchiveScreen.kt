
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Task
import com.siyu.notey.presentation.components.EmptyState
import com.siyu.notey.presentation.components.TaskCard

@Composable
fun ArchiveScreen(
    archivedTasks: List<Task>,
    minutesOf: (Int) -> Int,
    onBack: () -> Unit,
    onTaskClick: (Int) -> Unit,
    onRestore: (Int) -> Unit
) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Tareas archivadas", style = MaterialTheme.typography.headlineSmall)
        }

        if (archivedTasks.isEmpty()) {
            EmptyState(
                title = "Sin tareas archivadas",
                message = "Las tareas que archives aparecerán aquí y podrás restaurarlas."
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(archivedTasks, key = { it.id }) { task ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        TaskCard(
                            task = task,
                            loggedMinutes = minutesOf(task.id),
                            onClick = { onTaskClick(task.id) }
                        )
                        OutlinedButton(
                            onClick = { onRestore(task.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Restaurar") }
                    }
                }
            }
        }
    }
}