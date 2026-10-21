package com.siyu.notey.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ActivityRecord(
    val id: String,
    val taskId: Int,
    val date: String,
    val durationMinutes: Int,
    val note: String? = null,
    val isCompletion: Boolean = false
)
