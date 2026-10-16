package com.siyu.notey.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val category: Category,
    val priority: Priority,
    val status: TaskStatus,
    val createdAt: Long,
    val dueDate: String? = null,
    val estimatedMinutes: Int? = null,
    val isArchived: Boolean = false
)
