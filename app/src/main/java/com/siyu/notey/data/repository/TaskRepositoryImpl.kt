package com.siyu.notey.data.repository

import com.siyu.notey.data.local.TaskData
import com.siyu.notey.data.local.TaskDataStore
import com.siyu.notey.data.local.TaskSerializer
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
}