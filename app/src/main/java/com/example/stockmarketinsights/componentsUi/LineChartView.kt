package com.example.stockmarketinsights.componentsUi

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry

@Composable
fun LineChartView(
    entries: List<Entry>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)

                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisRight.isEnabled = false
                axisLeft.setDrawGridLines(false)
                xAxis.setDrawGridLines(false)
            }
        },
        update = { chart ->
            val dataSet = LineDataSet(entries, "Stock Price")
            dataSet.color = Color.BLUE
            dataSet.setCircleColor(Color.BLUE)
            dataSet.lineWidth = 2f
            dataSet.setDrawValues(false)

            chart.data = LineData(dataSet)
            chart.invalidate() // Refresh chart
        }
    )
}
