package com.example.stockmarketinsights.screensUi



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.viewmodel.StockViewModel
import com.example.stockmarketinsights.componentsUi.LineChartView

@Composable
fun ProductScreen(viewModel: StockViewModel) {
    val chartData by viewModel.chartData.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Stock Price Graph", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        if (chartData.isNotEmpty()) {
            LineChartView(
                entries = chartData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        } else {
            Text("Loading...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
