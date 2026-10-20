package com.siyu.notey.data.local

import com.google.android.libraries.places.api.model.LocalDate
import com.siyu.notey.data.model.ActivityRecord
import com.siyu.notey.data.model.Category
import com.siyu.notey.data.model.Priority
import com.siyu.notey.data.model.Task
import com.siyu.notey.data.model.TaskStatus

object SeedData {
    val categories = listOf(
        Category("Académico"),
        Category("Personal"),
        Category("Proyecto"),
        Category("Hábito")
    )

    private fun day(offset: Long) = LocalDate.now().plusDays(offset).toString()

    fun data(): TaskData {
        val now = System.currentTimeMillis()
        return TaskData(
            tasks = listOf(
                Task(1, "Practicar estructuras de datos", "Arreglos y recorridos", categories[0],
                    Priority.HIGH, TaskStatus.IN_PROGRESS, now, day(2), 90),
                Task(2, "Repasar cálculo multivariable", "", categories[0],
                    Priority.HIGH, TaskStatus.PENDING, now, day(4), 60),
                Task(3, "Diseñar pantallas de TaskFlow", "Mockups en Figma", categories[2],
                    Priority.MEDIUM, TaskStatus.IN_PROGRESS, now, day(5), 120),
                Task(4, "Leer un capítulo del libro", "", categories[1],
                    Priority.LOW, TaskStatus.COMPLETED, now, null, 30),
                Task(5, "Preparar entrega de móviles", "", categories[0],
                    Priority.HIGH, TaskStatus.PENDING, now, day(1), null),
                Task(6, "Rutina de ejercicio", "", categories[3],
                    Priority.LOW, TaskStatus.COMPLETED, now, null, 45, isArchived = true)
            ),
            activityRecords = listOf(
                ActivityRecord("act_1", 1, day(-1), 35, "Practiqué recorridos con for."),
                ActivityRecord("act_2", 1, day(0), 25, "Revisé condiciones y contadores."),
                ActivityRecord("act_3", 3, day(0), 40, "Ajusté tarjetas y navegación."),
                ActivityRecord("act_4", 4, day(-1), 30, "Lectura terminada."),
                ActivityRecord("act_5", 6, day(-3), 45, null)
            )
        )
    }
}