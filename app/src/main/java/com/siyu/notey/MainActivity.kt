
package com.siyu.notey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siyu.notey.data.local.TaskDataStore
import com.siyu.notey.data.model.Category
import com.siyu.notey.data.model.Priority
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus
import com.siyu.notey.data.repository.TaskRepositoryImpl
import com.siyu.notey.presentation.navigation.AppNav
import com.siyu.notey.presentation.screens.TaskScreen
import com.siyu.notey.presentation.state.TaskUiState
import com.siyu.notey.presentation.viewmodel.TaskViewModel
import com.siyu.notey.presentation.viewmodel.TaskViewModelFactory
import com.siyu.notey.ui.theme.NoteyTheme

class MainActivity : ComponentActivity() {


    //by viewModels crea el ViewModel una sola vez y lo conserva al girar la pantalla. Después AppNav(vm = taskViewModel) lo reparte a todas las pantallas, de modo que comparten el mismo estado.
    //
    //se usaa applicationContext y no this para el TaskDataStore, así el DataStore no retiene la Activity.
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            TaskRepositoryImpl(TaskDataStore(applicationContext))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteyTheme {
                AppNav(vm = taskViewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteyPreview() {
    val sampleTasks = listOf(
        Task(
            id = 1,
            title = "Estudiar Kotlin",
            description = "Practicar map y filter",
            category = Category.STUDY,
            priority = Priority.HIGH,
            status = TaskStatus.IN_PROGRESS,
            createdAt = 0L,
            estimatedMinutes = 60
        ),
        Task(
            id = 2,
            title = "Avanzar en Notey",
            description = "Diseñar la pantalla principal",
            category = Category.PROJECTS,
            priority = Priority.MEDIUM,
            status = TaskStatus.PENDING,
            createdAt = 0L,
            estimatedMinutes = 90
        )
    )

    MaterialTheme {
        TaskScreen(
            uiState = TaskUiState(
                tasks = sampleTasks,
                isLoading = false
            ),
            onCreateTask = { _, _, _, _, _ -> },
            onChangeStatus = { _, _ -> },
            onArchiveTask = {},
            onRegisterSession = {}
        )
    }
}
