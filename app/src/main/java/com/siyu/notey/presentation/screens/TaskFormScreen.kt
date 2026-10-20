
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Category
import com.siyu.notey.data.model.Priority

@Composable
fun TaskFormScreen(
    onCreateTask: (String, String, Category, Priority, Int?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Category.STUDY) }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var estimated by remember { mutableStateOf("30") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Crear tarea",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    error = null
                },
                label = { Text("Título *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Categoría")

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Category.entries.forEach { option ->
                    TextButton(onClick = { category = option }) {
                        Text(
                            if (category == option) "✓ ${option.name}"
                            else option.name
                        )
                    }
                }
            }

            Text("Prioridad")

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Priority.entries.forEach { option ->
                    TextButton(onClick = { priority = option }) {
                        Text(
                            if (priority == option) "✓ ${option.name}"
                            else option.name
                        )
                    }
                }
            }

            OutlinedTextField(
                value = estimated,
                onValueChange = {
                    estimated = it
                    error = null
                },
                label = { Text("Tiempo estimado (minutos, opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    val cleanTitle = title.trim()
                    val cleanEstimated = estimated.trim()
                    val minutes = cleanEstimated.toIntOrNull()

                    when {
                        cleanTitle.isBlank() -> {
                            error = "El título es obligatorio."
                        }

                        cleanEstimated.isNotEmpty() &&
                                (minutes == null || minutes <= 0) -> {
                            error = "Introduce un tiempo válido mayor que cero."
                        }

                        else -> {
                            onCreateTask(
                                cleanTitle,
                                description.trim(),
                                category,
                                priority,
                                minutes
                            )
                            title = ""
                            description = ""
                            category = Category.STUDY
                            priority = Priority.MEDIUM
                            estimated = "30"
                            error = null
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear tarea")
            }
        }
    }
}
