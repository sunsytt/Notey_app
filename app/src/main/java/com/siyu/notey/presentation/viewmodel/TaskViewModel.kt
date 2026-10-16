package com.siyu.notey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siyu.notey.data.repository.TaskRepository
import com.siyu.notey.presentation.state.TaskUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel (
    private val repository: TaskRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init{
        observeTasks()
    }

    private fun observeTasks(){
        viewModelScope.launch {
            repository.observeTaskData()
                .catch{ error ->
                    _uiState.update{
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                                ?: "No se pudieron cargar los datos."
                        )
                    }
                }
                .collect{ data ->
                    _uiState.update{
                        it.copy(
                            tasks = data.tasks,
                            activityRecords = data.activityRecords,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }
}