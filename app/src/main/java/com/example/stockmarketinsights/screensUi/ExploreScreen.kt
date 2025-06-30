package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.componentsUi.StockCard
import com.example.stockmarketinsights.dataModel.StockSummaryItem

@Composable
fun ExploreScreen(
    onViewAllGainersClick: () -> Unit = {},
    onViewAllLosersClick: () -> Unit = {},
    onStockClick: (StockSummaryItem) -> Unit = {}  // 👈 For navigation
) {
    val dummyGainers = remember {
        listOf(
            StockSummaryItem("Apple Inc.", "AAPL", "$195.23", "+2.1%"),
            StockSummaryItem("Tesla Inc.", "TSLA", "$254.32", "+5.3%"),
            StockSummaryItem("Netflix", "NFLX", "$410.23", "+1.4%"),
            StockSummaryItem("Meta", "META", "$298.56", "+3.6%")
        )
    }

    val dummyLosers = remember {
        listOf(
            StockSummaryItem("Intel", "INTC", "$34.12", "-2.5%"),
            StockSummaryItem("Nvidia", "NVDA", "$430.89", "-0.9%"),
            StockSummaryItem("Google", "GOOG", "$135.67", "-1.3%"),
            StockSummaryItem("Amazon", "AMZN", "$118.45", "-4.6%")
        )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Stocks App", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        SectionGridWithHeader(
            title = "Top Gainers",
            items = dummyGainers,
            onViewAllClick = onViewAllGainersClick,
            backgroundColor = Color(0xFFD0F5C9),
            onStockClick = onStockClick  // 👈 Pass navigation callback
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionGridWithHeader(
            title = "Top Losers",
            items = dummyLosers,
            onViewAllClick = onViewAllLosersClick,
            backgroundColor = Color(0xFFFADBD8),
            onStockClick = onStockClick  // 👈 Pass navigation callback
        )
    }
}

@Composable
fun SectionGridWithHeader(
    title: String,
    items: List<StockSummaryItem>,
    onViewAllClick: () -> Unit,
    backgroundColor: Color,
    onStockClick: (StockSummaryItem) -> Unit  // 👈 Required
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = onViewAllClick) {
            Text("View All")
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { stock ->
            StockCard(
                stock = stock,
                backgroundColor = backgroundColor,
                onClick = { onStockClick(stock) }  // 👈 Trigger detail screen
            )
        }
    }
}
