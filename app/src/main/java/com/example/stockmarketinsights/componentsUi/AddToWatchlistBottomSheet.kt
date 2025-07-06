package com.example.stockmarketinsights.componentsUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.roomdb.WatchlistEntity
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToWatchlistBottomSheet(
    stock: StockSummaryItem,
    sheetState: SheetState,
    onClose: () -> Unit,
    viewModel: WatchlistViewModel
) {
    val allWatchlists by viewModel.watchlistItems.collectAsState(initial = emptyList())
    val existingWatchlists = allWatchlists.map { it.watchlistName }.distinct()
    val selectedWatchlists = remember { mutableStateListOf<String>() }
    var newWatchlist by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        SnackbarHost(hostState = snackbarHostState)

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
                    selectedWatchlists.add(newWatchlist)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Temporarily added \"$newWatchlist\"")
                    }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = selectedWatchlists.contains(watchlist),
                        onValueChange = {
                            if (it) {
                                selectedWatchlists.add(watchlist)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Selected \"$watchlist\"")
                                }
                            } else {
                                selectedWatchlists.remove(watchlist)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Removed \"$watchlist\"")
                                }
                            }
                        }
                    )
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = selectedWatchlists.contains(watchlist),
                    onCheckedChange = null
                )
                Text(watchlist)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                selectedWatchlists.forEach { watchlistName ->
                    val entity = WatchlistEntity(
                        watchlistName = watchlistName,
                        stockName = stock.name,
                        stockSymbol = stock.symbol
                    )
                    viewModel.addWatchlistItem(entity)
                }
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Saved to: ${selectedWatchlists.joinToString()}")
                }
                onClose()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}
