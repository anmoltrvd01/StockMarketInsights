package com.example.stockmarketinsights.screensUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stockmarketinsights.componentsUi.AddToWatchlistBottomSheet
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(stock: StockSummaryItem, navController: NavController) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                    isSheetOpen = false
                }
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            AddToWatchlistBottomSheet(
                stock = stock,
                sheetState = sheetState,
                onClose = {
                    coroutineScope.launch {
                        sheetState.hide()
                        isSheetOpen = false
                    }
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stock Detail Screen") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            isSheetOpen = true
                            sheetState.show()
                        }
                    }) {
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
                        Text("${stock.symbol}, Common Stock", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stock.price,
                        color = if (stock.changePercent.startsWith("+")) Color(0xFF27AE60) else Color(0xFFC0392B),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stock.changePercent,
                        color = if (stock.changePercent.startsWith("+")) Color(0xFF27AE60) else Color(0xFFC0392B),
                        style = MaterialTheme.typography.bodySmall
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("1D", "1W", "1M", "3M", "6M", "1Y").forEach {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("About ${stock.name}", style = MaterialTheme.typography.titleSmall)
            Text(
                "This is a dummy description of ${stock.name}. Replace this with real company info from the API.",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text("Industry: Tech") })
                AssistChip(onClick = {}, label = { Text("Sector: AI") })
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Current price: ${stock.price}", style = MaterialTheme.typography.bodyMedium)
            Text("Profit Margin: 0.247 (Sample)", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
