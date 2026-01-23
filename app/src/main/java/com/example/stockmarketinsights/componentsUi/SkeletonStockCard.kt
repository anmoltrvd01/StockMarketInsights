package com.example.stockmarketinsights.componentsUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.*
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun SkeletonStockCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .shimmerEffect()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Transparent)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .width(140.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Transparent)
        )
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        ),
        label = "shimmerTranslate"
    )

    drawWithCache {
        val brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.3f),
                Color.LightGray.copy(alpha = 0.6f)
            ),
            start = androidx.compose.ui.geometry.Offset(translateAnim.value, 0f),
            end = androidx.compose.ui.geometry.Offset(translateAnim.value + 400f, 400f)
        )
        onDrawBehind {
            drawRect(brush)
        }
    }
}
