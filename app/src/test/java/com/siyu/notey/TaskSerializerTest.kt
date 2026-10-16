
package com.siyu.notey

import com.siyu.notey.data.local.TaskData
import com.siyu.notey.data.local.TaskSerializer
import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.model.Category
import com.siyu.notey.data.model.Priority
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus
import org.junit.Assert.assertEquals
import org.junit.Test


//prueba de serialización de datos
class TaskSerializerTest {

    @Test
    fun encodeAndDecode_preservesTaskData() {
        val original = TaskData(
            tasks = listOf(
                Task(
                    id = 1,
                    title = "Estudiar Kotlin",
                    description = "Practicar colecciones",
                    category = Category.STUDY,
                    priority = Priority.HIGH,
                    status = TaskStatus.IN_PROGRESS,
                    createdAt = 1000L,
                    estimatedMinutes = 60
                )
            ),
            activityRecords = listOf(
                ActivityRecord(
                    id = "record-1",
                    taskId = "task-1",
                    date = "2026-10-16",
                    durationMinutes = 30,
                    notes = "Practiqué map y filter"
                )
            )
        )

        val json = TaskSerializer.encode(original)
        val recovered = TaskSerializer.decode(json)

        assertEquals(original, recovered)
    }
}
