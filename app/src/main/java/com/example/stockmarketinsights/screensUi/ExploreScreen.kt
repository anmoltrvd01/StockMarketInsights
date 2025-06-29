package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockmarketinsights.viewmodel.ExploreViewModel
import com.example.stockmarketinsights.dataModel.StockSummaryItem

private const val API_KEY = "apikey"

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel,
    modifier: Modifier = Modifier
) {
    val gainers by viewModel.topGainers.collectAsState()
    val losers by viewModel.topLosers.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initData(apiKey = API_KEY)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        SectionWithStocks(
            title = "Top Gainers",
            stocks = gainers.take(4),
            loadingText = "Loading Gainers...",
            onViewAllClick = { /* TODO: Navigate to View All Gainers */ },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SectionWithStocks(
            title = "Top Losers",
            stocks = losers.take(4),
            loadingText = "Loading Losers...",
            onViewAllClick = { /* TODO: Navigate to View All Losers */ },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}

@Composable
fun SectionWithStocks(
    title: String,
    stocks: List<StockSummaryItem>,
    loadingText: String,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
            )
            TextButton(onClick = onViewAllClick) {
                Text("View All")
            }
        }

        if (stocks.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(stocks) { stock ->
                    StockCard(stock)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(loadingText)
            }
        }
    }
}

@Composable
fun StockCard(stock: StockSummaryItem) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(120.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stock.symbol, style = MaterialTheme.typography.titleMedium)
            Text(stock.name, maxLines = 1)
            Text("₹${stock.price}", style = MaterialTheme.typography.bodyLarge)
            Text(
                "${stock.changePercent}%",
                color = if (stock.changePercent > 0) Color(0xFF2E7D32) else Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
