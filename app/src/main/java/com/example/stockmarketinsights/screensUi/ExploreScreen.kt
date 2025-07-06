package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockmarketinsights.componentsUi.StockCard
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import com.example.stockmarketinsights.viewmodel.ExploreViewModel


@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    onViewAllGainersClick: () -> Unit = {},
    onViewAllLosersClick: () -> Unit = {},
    onStockClick: (StockSummaryItem) -> Unit = {},
    viewModel: ExploreViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filterBy by viewModel.filterBy.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    val filteredGainers = viewModel.allGainers.filter {
        matchesFilter(it, searchQuery, filterBy)
    }

    val filteredLosers = viewModel.allLosers.filter {
        matchesFilter(it, searchQuery, filterBy)
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            kotlinx.coroutines.delay(1000)
            isRefreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            isRefreshing = true
        }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Explore Stocks",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Search Stocks...") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    singleLine = true,
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.width(12.dp))

                FilterDropdown(
                    selected = filterBy,
                    onOptionSelected = { viewModel.updateFilterBy(it) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionGridWithHeader(
                title = "Top Gainers",
                items = filteredGainers,
                onViewAllClick = onViewAllGainersClick,
                backgroundColor = Color(0xFFD0F5C9),
                onStockClick = onStockClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            SectionGridWithHeader(
                title = "Top Losers",
                items = filteredLosers,
                onViewAllClick = onViewAllLosersClick,
                backgroundColor = Color(0xFFFADBD8),
                onStockClick = onStockClick
            )
        }
    }
}

@Composable
fun FilterDropdown(
    selected: String,
    onOptionSelected: (String) -> Unit
) {
    val options = listOf("Name", "Price", "Change")
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = "By $selected")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onOptionSelected(it)
                        expanded = false
                    }
                )
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


@Composable
fun SectionGridWithHeader(
    title: String,
    items: List<StockSummaryItem>,
    onViewAllClick: () -> Unit,
    backgroundColor: Color,
    onStockClick: (StockSummaryItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        )
        TextButton(onClick = onViewAllClick) {
            Text("View All")
        }
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { stock ->
            StockCard(
                stock = stock,
                backgroundColor = backgroundColor,
                onClick = { onStockClick(stock) },
            )
        }
    }
}
