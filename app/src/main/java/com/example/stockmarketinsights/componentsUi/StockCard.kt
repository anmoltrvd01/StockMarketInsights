package com.example.stockmarketinsights.componentsUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.dataModel.StockSummaryItem

@Composable
fun StockCard(
    stock: StockSummaryItem,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(end = 8.dp)
            .width(160.dp)
            .height(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stock.name, style = MaterialTheme.typography.labelLarge, color = Color.Black)
            Text(stock.symbol, style = MaterialTheme.typography.labelMedium, color = Color(0xFF4A4A4A))
            Text(stock.price, style = MaterialTheme.typography.bodyLarge, color = Color(0xFF1E88E5)) // Material blue
            Text(
                stock.changePercent,
                color = if (stock.changePercent.startsWith("+")) Color(0xFF2E7D32) else Color(0xFFC62828) // Green / Red
            )
        }
    }
}
