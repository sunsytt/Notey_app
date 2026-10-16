package com.siyu.notey.data.local

import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.model.Task
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

//convierte objetos kotlin en texto json y viceversa
@Serializable
data class TaskData(
    val tasks: List<Task> = emptyList(),
    val activityRecords: List<ActivityRecord> = emptyList()
)

object TaskSerializer {
    private val json = Json {
        prettyPrint = true
    }
    fun encode(data:TaskData): String {
        return json.encodeToString(data)
    }
    fun decode(raw:String): TaskData {
        return json.decodeFromString(raw)
    }
}