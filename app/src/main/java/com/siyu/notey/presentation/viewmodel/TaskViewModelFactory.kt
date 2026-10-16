package com.siyu.notey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.siyu.notey.data.repository.TaskRepository

class TaskViewModelFactory(
    private val repository: TaskRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido : ${modelClass.name}"
        )
    }
}