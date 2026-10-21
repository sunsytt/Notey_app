
package com.siyu.notey.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Category
import com.siyu.notey.data.model.Priority
import com.siyu.notey.data.model.Task
import com.siyu.notey.presentation.components.CategorySelector
import com.siyu.notey.presentation.components.PriorityChip
import com.siyu.notey.presentation.components.PrioritySelector
import java.time.LocalDate

@Composable
fun TaskFormScreen(
    taskToEdit: Task?,
    categories: List<Category>,
    onSave: (
        title: String,
        description: String,
        category: Category,
        priority: Priority,
        dueDate: String?,
        estimatedMinutes: Int?
    ) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(taskToEdit?.title ?: "") }
    var description by rememberSaveable { mutableStateOf(taskToEdit?.description ?: "") }
    var dueDate by rememberSaveable { mutableStateOf(taskToEdit?.dueDate ?: "") }
    var minutes by rememberSaveable { mutableStateOf(taskToEdit?.estimatedMinutes?.toString() ?: "") }
    var category by remember { mutableStateOf(taskToEdit?.category ?: categories.first()) }
    var priority by remember { mutableStateOf(taskToEdit?.priority ?: Priority.MEDIUM) }
    var triedToSave by remember { mutableStateOf(false) }

    // Validación derivada de los campos
    val titleError = title.isBlank()
    val parsedDate = if (dueDate.isBlank()) null else runCatching { LocalDate.parse(dueDate) }.getOrNull()
    val dateError = dueDate.isNotBlank() && parsedDate == null
    val parsedMinutes = minutes.toIntOrNull()
    val minutesError = minutes.isNotBlank() && (parsedMinutes == null || parsedMinutes <= 0)

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            if (taskToEdit == null) "Nueva tarea" else "Editar tarea",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título *") },
            isError = triedToSave && titleError,
            supportingText = { if (triedToSave && titleError) Text("El título es obligatorio") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción (opcional)") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Categoría", style = MaterialTheme.typography.labelLarge)
        CategorySelector(categories = categories, selected = category, onSelect = { category = it })

        Text("Prioridad", style = MaterialTheme.typography.labelLarge)
        PrioritySelector(selected = priority, onSelect = { priority = it })

        OutlinedTextField(
            value = dueDate,
            onValueChange = { dueDate = it },
            label = { Text("Fecha límite (aaaa-mm-dd, opcional)") },
            isError = dateError,
            supportingText = { if (dateError) Text("Usa el formato aaaa-mm-dd") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = minutes,
            onValueChange = { minutes = it.filter(Char::isDigit) },
            label = { Text("Tiempo estimado en minutos (opcional)") },
            isError = minutesError,
            supportingText = { if (minutesError) Text("Debe ser mayor que cero") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                triedToSave = true
                if (!titleError && !dateError && !minutesError) {
                    onSave(
                        title, description, category, priority,
                        parsedDate?.toString(),
                        parsedMinutes
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Guardar tarea") }

        TextButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
            Text("Cancelar")
        }
    }
}