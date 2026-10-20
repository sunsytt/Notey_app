package com.siyu.notey.presentation.screens

import android.R
import android.R.attr.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.presentation.state.TaskUiState

@Composable
fun HomeScreen (
    uiState: TaskUiState,
    onOpenTasks: () -> Unit,
    onOpenStatistics: () -> Unit,
    onOpenArchive: () -> Unit
){
    val activeTask = uiState.tasks.count { task ->
        !task.isArchived &&
                task.status != TaskStatus.COMPLETED
    }
    val completedTasks = uiState.tasks.count { task ->
        !task.isArchived &&
                task.status == TaskStatus.COMPLETED
    }
    val totalMinutes = uiState.activityRecords.sumOf {
        it.durationMinutes
    }

    Scaffold{
        padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                text = "Notey",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Organiza tus tareas y registra tus avances.",
                style = MaterialTheme.typography.bodyLarge
            )
            Card(
                modifier = Modifier.fillMaxWidth()
            ){
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Text(
                        text = "Tu resumen",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text ("Tareas activas: $activeTask")
                    Text ("Tareas completadas: $completedTasks")
                    Text ("Tiempo registrado: $totalMinutes min")
                }
            }
            Button(
                onClick = onOpenTasks,
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Mis tareas")
            }
            Button(
                onClick = onOpenStatistics,
                modifier = Modifier.fillMaxWidth()
            ){
                Text ("Estadisticas")
            }
            Button(
                onClick = onOpenArchive,
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Archivadas")
            }
        }
    }
}

