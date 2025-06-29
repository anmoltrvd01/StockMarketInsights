package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.stockmarketinsights.network.RetrofitInstance
import com.google.gson.JsonObject

class StockViewModel : ViewModel() {

    private val _chartData = MutableStateFlow<List<Entry>>(emptyList())
    val chartData: StateFlow<List<Entry>> = _chartData

    fun fetchStockData(symbol: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getStockPrices(
                    symbol = symbol,
                    apiKey = ""
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    val timeSeries = body?.getAsJsonObject("Time Series (Daily)")
                    val entries = timeSeries?.entrySet()?.take(7)?.mapIndexed { index, entry ->
                        val close = entry.value.asJsonObject["4. close"].asFloat
                        Entry(index.toFloat(), close)
                    } ?: emptyList()

                    _chartData.value = entries
                } else {
                    Log.e("StockViewModel", "API failed: ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                Log.e("StockViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }
}
