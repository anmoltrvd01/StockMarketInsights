package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockmarketinsights.dataModel.UiState
import com.example.stockmarketinsights.repository.StockRepository
import com.example.stockmarketinsights.roomdb.AppDatabase
import com.example.stockmarketinsights.viewmodel.ExploreViewModel
import com.example.stockmarketinsights.viewmodel.ExploreViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    onStockClick: (String) -> Unit = {}
) {

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val repository = remember { StockRepository(context = context, db = db) }

    val viewModel: ExploreViewModel = viewModel(
        factory = ExploreViewModelFactory(repository)
    )

    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchState by viewModel.searchResults.collectAsState()

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
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
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

            // Search bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::updateSearchQuery,
                    placeholder = { Text("Search stock (e.g. AAPL, TSLA)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Search results
            when (searchState) {

                is UiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is UiState.Error -> {
                    item {
                        Text(
                            text = (searchState as UiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is UiState.Success -> {
                    val stocks = (searchState as UiState.Success).data
                    if (searchQuery.isNotBlank()) {
                        if (stocks.isEmpty()) {
                            item { Text("No results found") }
                        } else {
                            items(stocks) { stock ->
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

            // Top Gainers
            if (searchQuery.isBlank() && gainers.isNotEmpty()) {
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
            if (searchQuery.isBlank() && losers.isNotEmpty()) {
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
