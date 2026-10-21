package com.siyu.notey.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.ActivityRecord

@Composable
fun SessionItem(
    record: ActivityRecord,
    modifier: Modifier = Modifier,
    taskTitle: String? = null   // el historial lo mostrará; el detalle no hace falta
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            taskTitle?.let { Text(it, style = MaterialTheme.typography.titleSmall) }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(record.date, style = MaterialTheme.typography.bodyMedium)
                Text(
                    "${record.durationMinutes} min",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            record.note?.let {
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}