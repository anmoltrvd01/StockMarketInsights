package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WatchlistScreen(
    onWatchlistClick: (String) -> Unit = {}  // Navigate on click
) {
    val dummyWatchlists = remember {
        listOf("Watchlist 1", "Watchlist 2")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Watchlist", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(dummyWatchlists) { name ->
                WatchlistCardSimple(name = name, onClick = {
                    onWatchlistClick(name)
                })
            }
        }
    }
}

@Composable
fun WatchlistCardSimple(name: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, style = MaterialTheme.typography.bodyLarge)
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open $name"
            )
        }
    }
}
