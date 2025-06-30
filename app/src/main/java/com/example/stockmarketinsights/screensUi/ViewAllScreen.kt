package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.componentsUi.StockCard
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import androidx.compose.ui.Alignment
@Composable
fun ViewAllScreen() {
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

    var currentPage by remember { mutableStateOf(1) }
    val pageSize = 6
    val pagedStocks = allStocks.take(currentPage * pageSize)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Top Gainers", style = MaterialTheme.typography.titleLarge)
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

        if (pagedStocks.size < allStocks.size) {
            Button(
                onClick = { currentPage++ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("Load More")
            }
        } else {
            Text(
                "No more stocks to load",
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp)
            )
        }
    }
}
