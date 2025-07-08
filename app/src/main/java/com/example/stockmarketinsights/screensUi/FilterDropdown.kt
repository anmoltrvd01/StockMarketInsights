package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun FilterDropdown(
    selected: String,
    onOptionSelected: (String) -> Unit
) {
    val options = listOf("Name", "Price", "Change")
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = "By $selected")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onOptionSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
