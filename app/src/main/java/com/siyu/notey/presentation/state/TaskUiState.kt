package com.siyu.notey.presentation.state

import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.model.Category
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
    val pendingCount get() = activeTasks.count { it.status == TaskStatus.PENDING }
    val inProgressCount get() = activeTasks.count { it.status == TaskStatus.IN_PROGRESS }
    val completedCount get() = activeTasks.count { it.status == TaskStatus.COMPLETED }

    val upcomingTasks get() = activeTasks
        .filter { it.status != TaskStatus.COMPLETED }
        .sortedWith(compareBy(nullsLast<String>()) { it.dueDate })
        .take(3)
    val filteredTasks get() = activeTasks
        .filter { selectedStatus == null || it.status == selectedStatus }
        .filter { selectedCategory == null || it.category == selectedCategory }

    val tasksWithEstimate  get() = activeTasks.filter { it.estimatedMinutes != null }
    val estimatedTotal get() = tasksWithEstimate.sumOf { it.estimatedMinutes ?: 0 }
    val realOfEstimated get() = tasksWithEstimate.sumOf { minutesOf(it.id) }

    val completionPercent get() =
        if (activeTasks.isEmpty()) 0
        else activeTasks.count { it.status == TaskStatus.COMPLETED } * 100 / activeTasks.size

    val totalMinutes get() = activeTasks.sumOf { minutesOf(it.id) }

    fun minutesOf(taskId: Int) =
        activityRecords.filter { it.taskId == taskId }.sumOf { it.durationMinutes }
}
