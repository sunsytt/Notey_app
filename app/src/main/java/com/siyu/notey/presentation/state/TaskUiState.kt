package com.siyu.notey.presentation.state

import com.google.ai.edge.litert.support.label.Category
import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus


data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val activityRecords: List<ActivityRecord> = emptyList(),
    val selectedStatus: TaskStatus? = null,
    val selectedCategory: Category? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val activeTasks get() = tasks.filter { !it.isArchived }
    val archivedTasks get() = tasks.filter { it.isArchived }

    val filteredTasks get() = activeTasks
        .filter { selectedStatus == null || it.status == selectedStatus }
        .filter { selectedCategory == null || it.category == selectedCategory }

    val completionPercent get() =
        if (activeTasks.isEmpty()) 0
        else activeTasks.count { it.status == TaskStatus.COMPLETED } * 100 / activeTasks.size

    val totalMinutes get() = activeTasks.sumOf { minutesOf(it.id) }

    fun minutesOf(taskId: Int) =
        activityRecords.filter { it.taskId == taskId }.sumOf { it.durationMinutes }
}

