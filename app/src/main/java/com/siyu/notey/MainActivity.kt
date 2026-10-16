
package com.siyu.notey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siyu.notey.data.local.TaskDataStore
import com.siyu.notey.data.repository.TaskRepositoryImpl
import com.siyu.notey.presentation.viewmodel.TaskViewModel
import com.siyu.notey.presentation.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = TaskDataStore(applicationContext)
        val repository = TaskRepositoryImpl(dataStore)
        val factory = TaskViewModelFactory(repository)

        setContent {
            val viewModel: TaskViewModel = viewModel(
                factory = factory
            )

            val uiState by viewModel.uiState
                .collectAsStateWithLifecycle()

            MaterialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "TaskFlow",
                            style = MaterialTheme.typography.headlineLarge
                        )

                        Text(
                            text = "Tus tareas y avances",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        when {
                            uiState.isLoading -> {
                                CircularProgressIndicator()
                            }

                            uiState.errorMessage != null -> {
                                Text(
                                    text = uiState.errorMessage
                                        ?: "Ocurrió un error.",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            uiState.tasks.isEmpty() -> {
                                Text(
                                    text = "Todavía no tienes tareas."
                                )
                            }

                            else -> {
                                LazyColumn(
                                    contentPadding = PaddingValues(
                                        vertical = 8.dp
                                    ),
                                    verticalArrangement =
                                        Arrangement.spacedBy(12.dp)
                                ) {
                                    items(
                                        items = uiState.tasks,
                                        key = { task -> task.id }
                                    ) { task ->
                                        Column {
                                            Text(
                                                text = task.title,
                                                style = MaterialTheme
                                                    .typography.titleMedium
                                            )

                                            Text(
                                                text = task.description
                                            )

                                            Text(
                                                text = "Estado: ${task.status}"
                                            )

                                            Text(
                                                text = "Prioridad: ${task.priority}"
                                            )

                                            Text(
                                                text = "Categoría: ${task.category}"
                                            )
                                        }

                                        HorizontalDivider(
                                            modifier = Modifier.padding(
                                                top = 12.dp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
