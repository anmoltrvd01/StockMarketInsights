package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.dialogsUi.AddToWatchlistDialog
import com.example.stockmarketinsights.roomdb.AppDatabase
import com.example.stockmarketinsights.roomdb.WatchlistRepository
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel
import com.example.stockmarketinsights.viewmodel.WatchlistViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    stock: StockSummaryItem,
    navController: NavController
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val repository = remember { WatchlistRepository(db.watchlistDao()) }

    val viewModel: WatchlistViewModel =
        viewModel(factory = WatchlistViewModelFactory(repository))

    val watchlistNames by viewModel.watchlistItems.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddToWatchlistDialog(
            showDialog = showDialog,
            existingWatchlists = watchlistNames
                .map { it.watchlistName }
                .distinct(),
            onDismiss = { showDialog = false },
            onAdd = { watchlistName ->
                viewModel.addStockToWatchlist(watchlistName, stock)
                showDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stock Detail Screen") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Bookmark")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(stock.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "${stock.symbol}, Common Stock",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stock.price,
                        color = if (stock.changePercent.startsWith("+"))
                            Color(0xFF27AE60) else Color(0xFFC0392B)
                    )
                    Text(
                        text = stock.changePercent,
                        color = if (stock.changePercent.startsWith("+"))
                            Color(0xFF27AE60) else Color(0xFFC0392B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("About ${stock.name}", style = MaterialTheme.typography.titleSmall)
            Text(
                "This is a dummy description of ${stock.name}. Replace later with real company info.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
