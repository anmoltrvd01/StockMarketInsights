package com.example.stockmarketinsights.dialogsUi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
        var selectedWatchlist by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("Add to Watchlist")
            },
            text = {
                Column {
                    Text("Create New Watchlist:")
                    OutlinedTextField(
                        value = newWatchlistName,
                        onValueChange = { newWatchlistName = it },
                        placeholder = { Text("Enter name...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Or Select Existing:")
                    existingWatchlists.forEach { watchlist ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedWatchlist = watchlist }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedWatchlist == watchlist,
                                onClick = { selectedWatchlist = watchlist }
                            )
                            Text(text = watchlist)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val resultName = if (newWatchlistName.isNotBlank()) newWatchlistName else selectedWatchlist
                        if (resultName.isNotBlank()) {
                            onAdd(resultName)
                        }
                        onDismiss()
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}
