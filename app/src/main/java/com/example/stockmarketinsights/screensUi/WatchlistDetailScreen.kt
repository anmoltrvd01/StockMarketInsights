package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.componentsUi.StockCard

@Composable
fun WatchlistDetailScreen(watchlistName: String) {
    val dummyStocks = remember {
        listOf(
            StockSummaryItem("Apple Inc.", "AAPL", "$195.23", "+2.1%"),
            StockSummaryItem("Tesla Inc.", "TSLA", "$254.32", "+5.3%"),
            StockSummaryItem("Netflix", "NFLX", "$410.23", "-1.4%"),
            StockSummaryItem("Meta", "META", "$298.56", "-3.6%"),
            StockSummaryItem("Intel", "INTC", "$34.12", "-2.5%"),
            StockSummaryItem("Nvidia", "NVDA", "$430.89", "+6.9%"),
            StockSummaryItem("Amazon", "AMZN", "$132.56", "+1.7%"),
            StockSummaryItem("Microsoft", "MSFT", "$322.50", "+0.9%")
        )
    }

    var currentPage by remember { mutableStateOf(1) }
    val pageSize = 6
    val pagedStocks = dummyStocks.take(currentPage * pageSize)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = watchlistName,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(pagedStocks) { stock ->
                StockCard(
                    stock = stock,
                    backgroundColor = if (stock.changePercent.startsWith("+")) Color(0xFFD0F5C9) else Color(0xFFFADBD8)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (pagedStocks.size < dummyStocks.size) {
            Button(
                onClick = { currentPage++ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Load More")
            }
        } else {
            Text(
                text = "No more stocks to load",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
        }
    }
}
