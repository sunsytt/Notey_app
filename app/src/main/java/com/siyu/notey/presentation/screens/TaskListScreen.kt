package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.presentation.components.TaskCard
import com.siyu.notey.presentation.state.TaskUiState

@Composable
fun TaskListScreen(
    uiState: TaskUiState,
    onChangeStatus: (Int, TaskStatus) -> Unit,
    onRegisterSession: (Int) -> Unit,
    onArchiveTask: (Int) -> Unit,
    onOpenDetails: (Int) -> Unit,
    onCreateTask: () -> Unit
) {
    val visibleTasks = uiState.tasks.filterNot { it.isArchived }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Mis tareas",
                style = MaterialTheme.typography.headlineMedium
            )

            Text("Tareas registradas: ${visibleTasks.size}")

            Text(
                text = "Selecciona una tarea para consultar sus detalles.",
                style = MaterialTheme.typography.bodyMedium
            )

            androidx.compose.material3.Button(
                onClick = onCreateTask
            ) {
                Text("Crear tarea")
            }

            if (visibleTasks.isEmpty()) {
                Text("Aún no tienes tareas. ¡Crea la primera!")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = visibleTasks,
                        key = { it.id }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onChangeStatus = onChangeStatus,
                            onRegisterSession = onRegisterSession,
                            onArchiveTask = onArchiveTask,
                            onOpenDetails = onOpenDetails
                        )
                    }
                }
            }
        }
    }
}
