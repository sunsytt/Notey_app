package com.siyu.notey.data.repository

import com.siyu.notey.data.local.TaskData
import com.siyu.notey.data.model.Task
import kotlinx.coroutines.flow.Flow
import com.siyu.notey.data.model.ActivityRecord

interface TaskRepository {
    fun observeTaskData(): Flow<TaskData>
    suspend fun seedIfEmpty()
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun archiveTask(taskId: Int)
    suspend fun restoreTask(taskId: Int)
    suspend fun addActivityRecord(record: ActivityRecord)
}