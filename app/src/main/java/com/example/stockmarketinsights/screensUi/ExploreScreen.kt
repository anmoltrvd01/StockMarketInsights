package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockmarketinsights.componentsUi.SkeletonStockCard
import com.example.stockmarketinsights.repository.StockRepository
import com.example.stockmarketinsights.roomdb.AppDatabase
import com.example.stockmarketinsights.viewmodel.ExploreViewModel
import com.example.stockmarketinsights.viewmodel.ExploreViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    onStockClick: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    viewModel: ExploreViewModel
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val repository = remember { StockRepository(context = context, db = db) }

    val viewModel: ExploreViewModel = viewModel(
        factory = ExploreViewModelFactory(repository)
    )

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Skeletons during initial load
            if (gainers.isEmpty() && losers.isEmpty()) {
                items(6) {
                    SkeletonStockCard()
                }
            }

            // Top Gainers
            if (gainers.isNotEmpty()) {
                item {
                    Text(
                        text = "Top Gainers",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                items(gainers) { stock ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onStockClick(stock.symbol) }
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(stock.symbol, fontWeight = FontWeight.Bold)
                            Text(stock.name)
                        }
                    }
                }
            }

            // Top Losers
            if (losers.isNotEmpty()) {
                item {
                    Text(
                        text = "Top Losers",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                items(losers) { stock ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onStockClick(stock.symbol) }
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(stock.symbol, fontWeight = FontWeight.Bold)
                            Text(stock.name)
                        }
                    }
                }
            }
        }
    }
}
