package com.siyu.notey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.repository.TaskRepository
import com.siyu.notey.presentation.state.TaskUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.siyu.notey.data.model.Category
import com.siyu.notey.data.model.Priority
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus
import java.time.LocalDate
import java.util.UUID


class TaskViewModel (
    private val repository: TaskRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { repository.seedIfEmpty() }
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

    fun createTask(
        title: String,
        description: String,
        category: Category,
        priority: Priority,
        estimatedMinutes: Int?
    ) {
        if (title.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "El título es obligatorio.")
            }
            return
        }

        if (estimatedMinutes != null && estimatedMinutes <= 0) {
            _uiState.update {
                it.copy(errorMessage = "El tiempo estimado debe ser mayor que cero.")
            }
            return
        }

        viewModelScope.launch {
            try {
                val task = Task(
                    id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                    title = title.trim(),
                    description = description.trim(),
                    category = category,
                    priority = priority,
                    status = TaskStatus.PENDING,
                    createdAt = System.currentTimeMillis(),
                    estimatedMinutes = estimatedMinutes
                )

                repository.addTask(task)
                _uiState.update { it.copy(errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "No se pudo guardar la tarea."
                    )
                }
            }
        }
    }
    fun updateTaskStatus(taskId: Int, newStatus: TaskStatus) {
        val task = _uiState.value.tasks
            .find { it.id == taskId }
            ?: return

        viewModelScope.launch {
            try {
                repository.updateTask(
                    task.copy(status = newStatus)
                )

                _uiState.update {
                    it.copy(errorMessage = null)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "No se pudo actualizar el estado."
                    )
                }
            }
        }
    }
    fun archiveTask(taskId: Int) {
        viewModelScope.launch {
            try {
                repository.archiveTask(taskId)
                _uiState.update { it.copy(errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "No se pudo archivar la tarea.")
                }
            }
        }
    }

    fun restoreTask(taskId: Int) {
        viewModelScope.launch {
            try {
                repository.restoreTask(taskId)
                _uiState.update { it.copy(errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "No se pudo restaurar la tarea.") }
            }
        }
    }
    fun registerSession(taskId: Int, date: String, minutes: Int, note: String?) {
        viewModelScope.launch {
            try {
                repository.addActivityRecord(
                    ActivityRecord(
                        id = UUID.randomUUID().toString(),
                        taskId = taskId,
                        date = date,
                        durationMinutes = minutes,
                        note = note?.trim()?.ifBlank { null }
                    )
                )
                _uiState.update { it.copy(errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "No se pudo registrar la sesión.")
                }
            }
        }
    }
    fun selectStatus(status: TaskStatus?) {
        _uiState.update { it.copy(selectedStatus = status) }
    }
    fun selectCategory(category: Category?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }
}
