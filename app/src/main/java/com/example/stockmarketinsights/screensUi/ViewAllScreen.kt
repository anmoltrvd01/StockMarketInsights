package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stockmarketinsights.componentsUi.StockCard
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.viewmodel.ExploreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllScreen(
    navController: NavController,
    category: String = "gainers", // "gainers" or "losers"
    viewModel: ExploreViewModel,
    onStockClick: (StockSummaryItem) -> Unit = {}
) {

    val stocks = when (category.lowercase()) {
        "gainers" -> viewModel.allGainers.collectAsState().value
        "losers" -> viewModel.allLosers.collectAsState().value
        else -> emptyList()
    }

    val isLoadingMore = when (category.lowercase()) {
        "gainers" -> viewModel.isLoadingMoreGainers.collectAsState().value
        "losers" -> viewModel.isLoadingMoreLosers.collectAsState().value
        else -> false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (category == "gainers") "Top Gainers" else "Top Losers",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            itemsIndexed(stocks) { index, stock ->

                // Pagination
                if (index == stocks.lastIndex - 2 && !isLoadingMore) {
                    if (category == "gainers") {
                        viewModel.loadMoreGainers()
                    } else {
                        viewModel.loadMoreLosers()
                    }
                }

                StockCard(
                    stock = stock,
                    cardColors = CardDefaults.cardColors(
                        containerColor =
                            if (stock.changePercent.startsWith("+"))
                                Color(0xFF27AE60)
                            else
                                Color(0xFFC0392B)
                    ),
                    onClick = { onStockClick(stock) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Loading footer
            if (isLoadingMore) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Loading more...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
