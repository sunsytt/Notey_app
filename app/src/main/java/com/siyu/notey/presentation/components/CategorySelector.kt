package com.siyu.notey.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siyu.notey.data.model.Category

@Composable
fun CategorySelector(
    categories: List<Category>,
    selected: Category?,
    onSelect: (Category) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        categories.forEach { category ->
            FilterChip(
                selected = category == selected,
                //category al ser un data class, == compara por contenido
                onClick = { onSelect(category) },
                label = { Text(category.name) }
            )
        }
    }
}