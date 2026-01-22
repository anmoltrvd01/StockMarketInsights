package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockmarketinsights.componentsUi.StockCard
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.roomdb.AppDatabase
import com.example.stockmarketinsights.roomdb.WatchlistRepository
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel
import com.example.stockmarketinsights.viewmodel.WatchlistViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistDetailScreen(
    watchlistName: String = "My Watchlist",
    navController: NavController,
    onStockClick: (StockSummaryItem) -> Unit = {}
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val repository = remember { WatchlistRepository(db.watchlistDao()) }
    val viewModel: WatchlistViewModel = viewModel(factory = WatchlistViewModelFactory(repository))

    val allItems by viewModel.watchlistItems.collectAsState()
    val stocksInWatchlist = allItems.filter { it.watchlistName == watchlistName }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(watchlistName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            if (stocksInWatchlist.isEmpty()) {
                Text(
                    "No stocks found in this watchlist.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(stocksInWatchlist) { item ->
                        val stock = StockSummaryItem(
                            name = item.stockName,
                            symbol = item.stockSymbol,
                            price = "N/A",
                            changePercent = "N/A"
                        )

                        Box(modifier = Modifier.fillMaxWidth()) {
                            StockCard(
                                stock = stock,
                                cardColors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                                onClick = { onStockClick(stock) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            IconButton(
                                onClick = {
                                    viewModel.deleteWatchlistItem(item)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "${item.stockSymbol} removed from '$watchlistName'"
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Stock"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
