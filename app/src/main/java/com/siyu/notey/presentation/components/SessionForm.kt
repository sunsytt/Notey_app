package com.siyu.notey.presentation.components

import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun SessionForm(
    onConfirm: (date:String, minutes: Int, note: String?) -> Unit,
    onDismiss: () -> Unit

){
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var minutes by remember { mutableStateOf("")}
    var note by remember { mutableStateOf("") }

    val parsedDate = runCatching { LocalDate.parse(date) }.getOrNull()
    val parsedMinutes = minutes.toIntOrNull()
    val isValid = parsedDate != null && parsedMinutes != null && parsedMinutes > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar sesión") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = date, onValueChange = { date = it },
                    label = { Text("Fecha (aaaa-mm-dd)") },
                    isError = parsedDate == null, singleLine = true
                )
                OutlinedTextField(
                    value = minutes, onValueChange = { minutes = it.filter(Char::isDigit) },
                    label = { Text("Duración (minutos)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                OutlinedTextField(
                    value = note, onValueChange = { note = it },
                    label = { Text("Nota (opcional)") }
                )
            }
        },
        confirmButton = {
            Button(
                enabled = isValid,
                onClick = { onConfirm(date, parsedMinutes!!, note.ifBlank { null }) }
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}


