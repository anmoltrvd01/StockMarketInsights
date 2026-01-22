package com.example.stockmarketinsights.componentsUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.stockmarketinsights.dataModel.StockSummaryItem

@Composable
fun StockCard(
    stock: StockSummaryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cardColors: CardColors = CardDefaults.cardColors()
) {
    Card(onClick = onClick, modifier = modifier, colors = cardColors) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stock.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = stock.symbol)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = stock.price, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                val changeColor = if (stock.changePercent.contains("-")) Color(0xFFD0F5C9) else Color(0xFFFADBD8)
                Text(text = "(${stock.changePercent})", color = changeColor)
            }
        }
    }
}