package com.siyu.notey.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


//guarda el texto y permite recuperarlo con taskflow
//lectura y escritura del JSON en el dispositivo

private val Context.taskDataStore by preferencesDataStore(
    name = "notey_data"
)
private val TASK_DATA_KEY = stringPreferencesKey ("task_data_json")

class TaskDataStore (
    private val context: Context
) {
    //crea un flujo de datos para leer el texto
    val taskDataFlow: Flow<String?> =
        context.taskDataStore.data.map { preferences ->
            preferences[TASK_DATA_KEY]
        }
    //guarda el texto en las preferencias de datos
    suspend fun saveTaskData(json:String) {
        context.taskDataStore.edit { preferences ->
            preferences[TASK_DATA_KEY] = json
        }
    }
    suspend fun updateTaskData(
        transform: (String?) -> String
    ) {
        context.taskDataStore.edit { preferences ->
            val currentJson = preferences[TASK_DATA_KEY]
            preferences[TASK_DATA_KEY] = transform(currentJson)
        }
    }

}