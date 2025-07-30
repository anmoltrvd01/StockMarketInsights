package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.example.stockmarketinsights.viewmodel.ExploreViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    onViewAllGainersClick: () -> Unit = {},
    onViewAllLosersClick: () -> Unit = {},
    onStockClick: (StockSummaryItem) -> Unit = {},
    onSearchClick: () -> Unit = {},
    viewModel: ExploreViewModel = viewModel()
) {
    var isRefreshing by remember { mutableStateOf(false) }

    val allGainers by viewModel.allGainers.collectAsState()
    val allLosers by viewModel.allLosers.collectAsState()
    val trendingStocks = (allGainers + allLosers).shuffled().take(4)

    val marketMood = when {
        allGainers.size > allLosers.size -> "Bullish"
        allLosers.size > allGainers.size -> "Bearish"
        else -> "Neutral"
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000)
            isRefreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { isRefreshing = true }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "StockMarketInsights",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    actions = {
                        IconButton(onClick = onSearchClick) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { MarketMoodBanner(marketMood) }

                item { MarketIndexWidget(nifty = "22,753.80", sensex = "75,410.39") }

                item {
                    SectionGridWithHeader(
                        title = "Trending Now",
                        items = trendingStocks,
                        onViewAllClick = {}, // No View All
                        backgroundColor = Color.Transparent, // Will be dynamic inside
                        onStockClick = onStockClick,
                        showViewAll = false,
                        dynamicColorByChange = true
                    )
                }

                item {
                    SectionGridWithHeader(
                        title = "Top Gainers",
                        items = allGainers,
                        onViewAllClick = onViewAllGainersClick,
                        backgroundColor = Color(0xFFB2FFCC),
                        onStockClick = onStockClick
                    )
                }

                item {
                    SectionGridWithHeader(
                        title = "Top Losers",
                        items = allLosers,
                        onViewAllClick = onViewAllLosersClick,
                        backgroundColor = Color(0xFFFFD9D9),
                        onStockClick = onStockClick
                    )
                }
            }
        }
    }
}

@Composable
fun MarketMoodBanner(mood: String) {
    val isDark = isSystemInDarkTheme()

    val backgroundColor = when (mood) {
        "Bullish" -> if (isDark) Color(0xFF2E7D32) else Color(0xFFC8E6C9)
        "Bearish" -> if (isDark) Color(0xFFC62828) else Color(0xFFFFCDD2)
        else -> if (isDark) Color(0xFF546E7A) else Color(0xFFCFD8DC)
    }

    val moodText = when (mood) {
        "Bullish" -> "Market is trending up 📈 "
        "Bearish" -> "Market is trending down 📉 "
        else -> "Market is stable"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier.padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = moodText,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun MarketIndexWidget(nifty: String, sensex: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Nifty 50", style = MaterialTheme.typography.labelMedium)
                Text(nifty, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Divider(
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Sensex", style = MaterialTheme.typography.labelMedium)
                Text(sensex, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun SectionGridWithHeader(
    title: String,
    items: List<StockSummaryItem>,
    onViewAllClick: () -> Unit,
    backgroundColor: Color,
    onStockClick: (StockSummaryItem) -> Unit,
    showViewAll: Boolean = true,
    dynamicColorByChange: Boolean = false
) {
    val displayItems = items.take(6)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Badge {
                    Text(items.size.toString(), fontSize = 12.sp)
                }
            }

            if (showViewAll) {
                TextButton(onClick = onViewAllClick) {
                    Text("View All")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 400.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            userScrollEnabled = false
        ) {
            items(displayItems) { stock ->
                val dynamicBg = if (dynamicColorByChange) {
                    if (stock.changePercent.contains("-")) Color(0xFFFFD9D9) else Color(0xFFB2FFCC)
                } else backgroundColor

                StockCard(
                    stock = stock,
                    backgroundColor = dynamicBg,
                    onClick = { onStockClick(stock) }
                )
            }
        }
    }
}
