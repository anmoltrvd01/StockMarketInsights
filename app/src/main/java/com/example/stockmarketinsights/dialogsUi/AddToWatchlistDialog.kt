package com.example.stockmarketinsights.dialogsUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddToWatchlistDialog(
    showDialog: Boolean,
    existingWatchlists: List<String>,
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    if (showDialog) {
        var newWatchlistName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Add to Watchlist") },
            text = {
                Column {
                    existingWatchlists.forEach {
                        Button(onClick = { onAdd(it) }) {
                            Text(it)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = newWatchlistName,
                        onValueChange = { newWatchlistName = it },
                        label = { Text("New Watchlist Name") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { onAdd(newWatchlistName) },
                    enabled = newWatchlistName.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}