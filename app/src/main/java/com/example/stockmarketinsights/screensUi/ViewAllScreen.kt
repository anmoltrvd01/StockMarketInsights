package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stockmarketinsights.componentsUi.StockCard
import com.example.stockmarketinsights.dataModel.StockSummaryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllScreen(
    navController: NavController,
    category: String = "gainers", // "gainers" or "losers"
    onStockClick: (StockSummaryItem) -> Unit = {}
) {
    val allStocks = remember {
        listOf(
            StockSummaryItem("Tesla Inc.", "TSLA", "$254.32", "+5.3%"),
            StockSummaryItem("Apple Inc.", "AAPL", "$195.23", "+2.1%"),
            StockSummaryItem("Nvidia Corp.", "NVDA", "$430.89", "+6.9%"),
            StockSummaryItem("Meta Platforms", "META", "$298.56", "-3.6%"),
            StockSummaryItem("Netflix Inc.", "NFLX", "$410.23", "-2.3%"),
            StockSummaryItem("Intel Corp.", "INTC", "$34.12", "-4.5%"),
            StockSummaryItem("Google LLC", "GOOGL", "$2700.00", "+1.2%"),
            StockSummaryItem("Amazon", "AMZN", "$120.45", "-1.8%")
        )
    }

    val filteredStocks = when (category.lowercase()) {
        "gainers" -> allStocks.filter { it.changePercent.startsWith("+") }
        "losers" -> allStocks.filter { it.changePercent.startsWith("-") }
        else -> allStocks
    }

    var currentPage by remember { mutableStateOf(1) }
    val pageSize = 6
    val pagedStocks = filteredStocks.take(currentPage * pageSize)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(if (category == "gainers") "Top Gainers" else "Top Losers")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
                        backgroundColor = if (stock.changePercent.startsWith("+")) Color(0xFFD0F5C9) else Color(0xFFFADBD8),
                        onClick = { onStockClick(stock) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (pagedStocks.size < filteredStocks.size) {
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
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}
