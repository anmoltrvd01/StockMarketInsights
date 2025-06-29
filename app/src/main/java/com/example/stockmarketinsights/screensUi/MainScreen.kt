package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.example.stockmarketinsights.viewmodel.ExploreViewModel
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel

@Composable
fun MainScreen(
    exploreViewModel: ExploreViewModel,
    watchlistViewModel: WatchlistViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            if (selectedTab == 0) { // Only show top bar on Explore screen
                AppTopBar()
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("Watchlist") },
                    icon = { Icon(Icons.Default.List, contentDescription = "Watchlist") }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> ExploreScreen(
                viewModel = exploreViewModel,
                modifier = Modifier.padding(padding)
            )
            1 -> WatchlistScreen(
                viewModel = watchlistViewModel,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "StockInsights",
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search here...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .width(180.dp)
                        .height(50.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}
