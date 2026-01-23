package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stockmarketinsights.componentsUi.StockCard
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.viewmodel.ExploreViewModel
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistDetailScreen(
    watchlistName: String,
    navController: NavController,
    watchlistViewModel: WatchlistViewModel,
    exploreViewModel: ExploreViewModel,
    onStockClick: (StockSummaryItem) -> Unit = {}
) {
    val allWatchlistItems by watchlistViewModel.watchlistItems.collectAsState()
    val stocksInWatchlist = allWatchlistItems.filter { it.watchlistName == watchlistName }

    val gainers by exploreViewModel.allGainers.collectAsState()
    val losers by exploreViewModel.allLosers.collectAsState()
    val allStocks = gainers + losers

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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No stocks in this watchlist yet.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(stocksInWatchlist) { item ->
                        val stockInfo = allStocks.find { it.symbol == item.stockSymbol }

                        val stock = StockSummaryItem(
                            name = item.stockName,
                            symbol = item.stockSymbol,
                            price = stockInfo?.price ?: "N/A",
                            changePercent = stockInfo?.changePercent ?: "N/A"
                        )

                        Box(modifier = Modifier.fillMaxWidth()) {
                            StockCard(
                                stock = stock,
                                onClick = { onStockClick(stock) },
                                modifier = Modifier.fillMaxWidth(),
                                cardColors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                            IconButton(
                                onClick = {
                                    watchlistViewModel.deleteWatchlistItem(item)
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
