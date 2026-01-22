package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockmarketinsights.componentsUi.SkeletonStockCard
import com.example.stockmarketinsights.componentsUi.StockCard
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.viewmodel.ExploreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel,
    onStockClick: (StockSummaryItem) -> Unit,
    onSearchClick: () -> Unit,
    onViewAllGainers: () -> Unit,
    onViewAllLosers: () -> Unit
) {
    val gainers by viewModel.allGainers.collectAsState()
    val losers by viewModel.allLosers.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllStocks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "StockMarketInsights",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Stocks"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Skeletons during initial load
            if (gainers.isEmpty() && losers.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Top Gainers",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        TextButton(onClick = onViewAllGainers) {
                            Text("View All")
                        }
                    }
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(250.dp) // Approximate height for 2 rows
                    ) {
                        items(4) {
                            SkeletonStockCard()
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Top Losers",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        TextButton(onClick = onViewAllLosers) {
                            Text("View All")
                        }
                    }
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(300.dp) // Approximate height for 2 rows
                    ) {
                        items(4) {
                            SkeletonStockCard()
                        }
                    }
                }
            }


            // Top Gainers
            if (gainers.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Top Gainers",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        TextButton(onClick = onViewAllGainers) {
                            Text("View All")
                        }
                    }
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(320.dp) // Adjust height as needed
                    ) {
                        items(gainers.take(4)) { stock ->
                            StockCard(stock = stock, onClick = { onStockClick(stock) })
                        }
                    }
                }
            }

            // Top Losers
            if (losers.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Top Losers",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        TextButton(onClick = onViewAllLosers) {
                            Text("View All")
                        }
                    }
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(320.dp) // Adjust height as needed
                    ) {
                        items(losers.take(4)) { stock ->
                            StockCard(stock = stock, onClick = { onStockClick(stock) })
                        }
                    }
                }
            }
        }
    }
}
