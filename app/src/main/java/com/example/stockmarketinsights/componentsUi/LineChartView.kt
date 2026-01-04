package com.example.stockmarketinsights.componentsUi

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun LineChartView(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setTouchEnabled(true)
                setPinchZoom(true)
                description = Description().apply { text = "Price History" }
                setNoDataText("No chart data available")
            }
        },
        modifier = modifier,
        update = { chart ->
            val entries = listOf(
                Entry(0f, 240f),
                Entry(1f, 245f),
                Entry(2f, 250f),
                Entry(3f, 252f),
                Entry(4f, 254f)
            )

            val dataSet = LineDataSet(entries, "Price").apply {
                color = Color.Blue.hashCode()
                valueTextColor = Color.Black.hashCode()
                lineWidth = 2f
                circleRadius = 4f
                setCircleColor(Color.Blue.hashCode())
            }

            val lineData = LineData(dataSet)
            chart.data = lineData
            chart.invalidate()
        }
    )
}





