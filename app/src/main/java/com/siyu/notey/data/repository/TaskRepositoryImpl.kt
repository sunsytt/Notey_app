package com.siyu.notey.data.repository

import com.siyu.notey.data.local.TaskData
import com.siyu.notey.data.local.TaskDataStore
import com.siyu.notey.data.local.TaskSerializer
import com.siyu.notey.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val dataStore: TaskDataStore
) : TaskRepository {
    override fun observeTaskData(): Flow<TaskData> {
        return dataStore.taskDataFlow.map { raw ->
            if (raw == null) {
                TaskData()
            } else {
                TaskSerializer.decode(raw)
            }
        }
    }

    override suspend fun saveTaskData(data: TaskData) {
        val json = TaskSerializer.encode(data)
        dataStore.saveTaskData(json)
    }
    override suspend fun addTask(task: Task) {
        dataStore.updateTaskData { raw ->
            val currentData = if (raw == null) {
                TaskData()
            } else {
                TaskSerializer.decode(raw)
            }

            val updatedData = currentData.copy(
                tasks = currentData.tasks + task
            )

            TaskSerializer.encode(updatedData)
        }
    }
    override suspend fun updateTask(task: Task) {
        dataStore.updateTaskData { raw ->
            val currentData = if (raw == null) {
                TaskData()
            } else {
                TaskSerializer.decode(raw)
            }

            val updatedTasks = currentData.tasks.map { currentTask ->
                if (currentTask.id == task.id) {
                    task
                } else {
                    currentTask
                }
            }

            TaskSerializer.encode(
                currentData.copy(tasks = updatedTasks)
            )
        }
    }
}