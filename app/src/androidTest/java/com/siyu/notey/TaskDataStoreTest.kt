
package com.siyu.notey

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.siyu.notey.data.local.TaskDataStore
import com.siyu.notey.data.local.TaskSerializer
import com.siyu.notey.data.local.TaskData
import com.siyu.notey.data.model.Category
import com.siyu.notey.data.model.Priority
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDataStoreTest {

    @Test
    fun saveAndRead_returnsSavedData() = runBlocking {
        val testContext =
            InstrumentationRegistry.getInstrumentation().context

        val dataStore = TaskDataStore(testContext)

        val originalData = TaskData(
            tasks = listOf(
                Task(
                    id = 1,
                    title = "Probar DataStore",
                    description = "Verificar persistencia",
                    category = Category.STUDY,
                    priority = Priority.HIGH,
                    status = TaskStatus.PENDING,
                    createdAt = 1000L,
                    estimatedMinutes = 30
                )
            )
        )

        val json = TaskSerializer.encode(originalData)

        dataStore.saveTaskData(json)

        val savedJson = dataStore.taskDataFlow.first()
        val recoveredData = TaskSerializer.decode(savedJson!!)

        assertEquals(originalData, recoveredData)
    }
}

