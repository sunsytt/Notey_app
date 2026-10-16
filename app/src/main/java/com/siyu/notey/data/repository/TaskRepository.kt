package com.siyu.notey.data.repository

import com.siyu.notey.data.local.TaskData
import com.siyu.notey.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTaskData(): Flow<TaskData>
    suspend fun saveTaskData(data: TaskData)
    suspend fun addTask(task: Task)

}