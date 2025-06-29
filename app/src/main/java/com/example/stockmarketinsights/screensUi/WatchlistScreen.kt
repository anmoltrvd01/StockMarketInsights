package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.roomdb.WatchlistEntity
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel

@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.watchlistItems.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (items.isEmpty()) {
            // Empty State
            Text(
                text = "Your watchlist is empty.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column {
                Text(
                    text = "Your Watchlist",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items) { stock ->
                        WatchlistItemCard(stock = stock) {
                            viewModel.removeFromWatchlist(stock)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WatchlistItemCard(stock: WatchlistEntity, onRemoveClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(stock.symbol, style = MaterialTheme.typography.titleMedium)
                Text("₹${stock.price}", style = MaterialTheme.typography.bodyMedium)
            }
            OutlinedButton(onClick = onRemoveClick) {
                Text("Remove")
            }
        }
    }
}
