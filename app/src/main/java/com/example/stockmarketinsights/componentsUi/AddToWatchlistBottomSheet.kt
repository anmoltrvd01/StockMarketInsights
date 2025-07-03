package com.example.stockmarketinsights.componentsUi

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.dataModel.StockSummaryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToWatchlistBottomSheet(
    stock: StockSummaryItem,
    sheetState: SheetState,
    onClose: () -> Unit
) {
    val existingWatchlists = remember { mutableStateListOf("Watchlist 1", "Watchlist 2") }
    val selectedWatchlists = remember { mutableStateListOf<String>() }
    var newWatchlist by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add to Watchlist", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Row {
            TextField(
                value = newWatchlist,
                onValueChange = { newWatchlist = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("New Watchlist Name") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newWatchlist.isNotBlank()) {
                    existingWatchlists.add(newWatchlist)
                    selectedWatchlists.add(newWatchlist)
                    newWatchlist = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        existingWatchlists.forEach { watchlist ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = selectedWatchlists.contains(watchlist),
                    onCheckedChange = {
                        if (it) selectedWatchlists.add(watchlist)
                        else selectedWatchlists.remove(watchlist)
                    }
                )
                Text(watchlist)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // simulate saving logic
                onClose()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}
