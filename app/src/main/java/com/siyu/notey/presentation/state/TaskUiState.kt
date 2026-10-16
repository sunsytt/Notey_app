package com.siyu.notey.presentation.state

import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.model.Task


data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val activityRecords: List<ActivityRecord> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

