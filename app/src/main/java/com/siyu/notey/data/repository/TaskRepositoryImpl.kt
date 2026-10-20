package com.siyu.notey.data.repository

import com.siyu.notey.data.local.SeedData
import com.siyu.notey.data.local.TaskData
import com.siyu.notey.data.local.TaskDataStore
import com.siyu.notey.data.local.TaskSerializer
import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

class TaskRepositoryImpl(
    private val dataStore: TaskDataStore
) : TaskRepository {

    override fun observeTaskData(): Flow<TaskData> =
        dataStore.taskDataFlow.map { raw ->
            raw?.let(TaskSerializer::decode) ?: TaskData()
        }

    // Solo escribe si no hay nada guardado; nunca pisa datos del usuario
    override suspend fun seedIfEmpty() {
        dataStore.updateTaskData { raw ->
            raw ?: TaskSerializer.encode(SeedData.data())
        }
    }

    // Lee, transforma y guarda en una sola operación atómica
    private suspend fun modify(transform: (TaskData) -> TaskData) {
        dataStore.updateTaskData { raw ->
            val current = raw?.let(TaskSerializer::decode) ?: TaskData()
            TaskSerializer.encode(transform(current))
        }
    }

    override suspend fun addTask(task: Task) = modify {
        it.copy(tasks = it.tasks + task)
    }

    override suspend fun updateTask(task: Task) = modify { data ->
        data.copy(tasks = data.tasks.map { if (it.id == task.id) task else it })
    }

    override suspend fun archiveTask(taskId: Int) = modify { data ->
        data.copy(tasks = data.tasks.map {
            if (it.id == taskId) it.copy(isArchived = true) else it
        })
    }

    // Conserva el estado anterior (RN-09)
    override suspend fun restoreTask(taskId: Int) = modify { data ->
        data.copy(tasks = data.tasks.map {
            if (it.id == taskId) it.copy(isArchived = false) else it
        })
    }

    override suspend fun addActivityRecord(record: ActivityRecord) = modify { data ->
        require(record.durationMinutes > 0) { "La duración debe ser mayor que cero." }
        require(data.tasks.any { it.id == record.taskId }) { "La tarea no existe." }

        data.copy(
            // RN-16: Pendiente pasa a En progreso; los demás estados no cambian
            tasks = data.tasks.map {
                if (it.id == record.taskId && it.status == TaskStatus.PENDING)
                    it.copy(status = TaskStatus.IN_PROGRESS)
                else it
            },
            activityRecords = data.activityRecords + record
        )
    }
}