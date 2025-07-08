package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    onWatchlistClick: (String) -> Unit = {},
    onStockClick: (StockSummaryItem) -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val allItems by viewModel.watchlistItems.collectAsState()
    val distinctWatchlists = allItems.map { it.watchlistName }.distinct()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var renameDialogOpen by remember { mutableStateOf(false) }
    var watchlistToRename by remember { mutableStateOf("") }
    var newWatchlistName by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Watchlists") },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search Watchlists")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            LazyColumn {
                items(distinctWatchlists) { name ->
                    WatchlistCardSimple(
                        name = name,
                        onClick = {
                            val stock = allItems.find { it.watchlistName == name }
                            stock?.let {
                                onStockClick(
                                    StockSummaryItem(
                                        it.stockName,
                                        it.stockSymbol,
                                        price = "N/A",
                                        changePercent = "N/A"
                                    )
                                )
                            }
                        },
                        onWatchlistClick = { onWatchlistClick(name) },
                        onRenameClick = {
                            watchlistToRename = it
                            newWatchlistName = it
                            renameDialogOpen = true
                        },
                        onDeleteClick = { nameToDelete ->
                            viewModel.deleteWatchlistByName(nameToDelete)
                            scope.launch {
                                snackbarHostState.showSnackbar("Deleted '$nameToDelete'")
                            }
                        }
                    )
                }
            }
        }
    }

    if (renameDialogOpen) {
        AlertDialog(
            onDismissRequest = { renameDialogOpen = false },
            title = { Text("Rename Watchlist") },
            text = {
                OutlinedTextField(
                    value = newWatchlistName,
                    onValueChange = { newWatchlistName = it },
                    label = { Text("New name") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.renameWatchlist(watchlistToRename, newWatchlistName)
                    renameDialogOpen = false
                    scope.launch {
                        snackbarHostState.showSnackbar("Renamed to '$newWatchlistName'")
                    }
                }) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    renameDialogOpen = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun WatchlistCardSimple(
    name: String,
    onClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onRenameClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onWatchlistClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(name, style = MaterialTheme.typography.bodyLarge)
                Text("Tap arrow icon to view first stock", style = MaterialTheme.typography.bodySmall)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open stock"
                    )
                }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options")
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Rename") },
                            onClick = {
                                showMenu = false
                                onRenameClick(name)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                showMenu = false
                                onDeleteClick(name)
                            }
                        )
                    }
                }
            }
        }
    }
}
