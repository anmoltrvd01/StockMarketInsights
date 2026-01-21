package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockmarketinsights.componentsUi.StockCard
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.viewmodel.ExploreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAllStocksScreen(
    viewModel: ExploreViewModel,
    onStockClick: (StockSummaryItem) -> Unit,
    onBack: () -> Unit,

) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filterBy by viewModel.filterBy.collectAsState()

    val allGainers by viewModel.allGainers.collectAsState()
    val allLosers by viewModel.allLosers.collectAsState()
    val allStocks = allGainers + allLosers

    val filteredStocks = allStocks.filter {
        matchesFilter(it, searchQuery, filterBy)
    }

    val scrollState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Search Stocks",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search & Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Search by $filterBy...") },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = MaterialTheme.shapes.large,
                    singleLine = true,
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                FilterDropdown(
                    selected = filterBy,
                    onOptionSelected = { viewModel.updateFilterBy(it) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (filteredStocks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 64.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "No matching stocks found.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    state = scrollState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredStocks) { stock ->
                        val backgroundColor = when {
                            stock.changePercent.contains("-") -> Color(0xFFFFCDD2) // red tone
                            stock.changePercent.contains("+") || stock.changePercent.toDoubleOrNull() ?: 0.0 > 0.0 -> Color(0xFFC8E6C9) // green tone
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }

                        StockCard(
                            stock = stock,
                            backgroundColor = backgroundColor,
                            onClick = { onStockClick(stock) }
                        )
                    }
                }
            }
        }
    }
}

fun matchesFilter(item: StockSummaryItem, query: String, filterBy: String): Boolean {
    val lowerQuery = query.lowercase()

    return when (filterBy.lowercase()) {
        "name" -> item.name.lowercase().contains(lowerQuery) || item.symbol.lowercase().contains(lowerQuery)
        "price" -> item.price.filter { it.isDigit() || it == '.' }.contains(lowerQuery)
        "change" -> item.changePercent.filter { it.isDigit() || it == '.' || it == '-' || it == '+' }.contains(lowerQuery)
        else -> true
    }
}
