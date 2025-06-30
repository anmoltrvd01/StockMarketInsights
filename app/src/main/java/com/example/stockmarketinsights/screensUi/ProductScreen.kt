package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.componentsUi.LineChartView
import com.example.stockmarketinsights.dataModel.StockDetail
import com.example.stockmarketinsights.dialogsUi.AddToWatchlistDialog

@Composable
fun ProductScreen() {
    val stock = remember {
        StockDetail(
            name = "Tesla Inc.",
            symbol = "TSLA",
            price = "$254.32",
            marketCap = "800B"
        )
    }

    var isInWatchlist by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // Dummy existing watchlists
    val dummyWatchlists = listOf("Tech Picks", "Favorites", "Long-Term")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stock.name, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text("(${stock.symbol})", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Price: ${stock.price}", style = MaterialTheme.typography.bodyLarge)
        Text("Market Cap: ${stock.marketCap}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Chart
        LineChartView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Add/Remove Button
        Button(
            onClick = {
                if (isInWatchlist) {
                    isInWatchlist = false
                } else {
                    showDialog = true
                }
            }
        ) {
            Icon(
                imageVector = if (isInWatchlist) Icons.Default.Check else Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isInWatchlist) "Remove from Watchlist" else "Add to Watchlist")
        }
    }

    // AddToWatchlistDialog
    if (showDialog) {
        AddToWatchlistDialog(
            showDialog = true,
            existingWatchlists = dummyWatchlists,
            onDismiss = { showDialog = false },
            onAdd = { selected ->
                // Later: Save to RoomDB here
                isInWatchlist = true
                showDialog = false
            }
        )
    }
}
