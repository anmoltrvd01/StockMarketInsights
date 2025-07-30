package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketinsights.repository.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {

    private val repository = StockRepository()

    private val _quotes = MutableStateFlow<Map<String, String>>(emptyMap())
    val quotes: StateFlow<Map<String, String>> = _quotes

    private val _overviews = MutableStateFlow<Map<String, String>>(emptyMap())
    val overviews: StateFlow<Map<String, String>> = _overviews

    private val _timeSeries = MutableStateFlow<Map<String, String>>(emptyMap())
    val timeSeries: StateFlow<Map<String, String>> = _timeSeries

    fun loadQuotes(symbols: List<String>) {
        viewModelScope.launch {
            val result = repository.getMultipleQuotes(symbols)
            result?.let { _quotes.value = it }
        }
    }

    fun loadOverviews(symbols: List<String>) {
        viewModelScope.launch {
            val result = repository.getMultipleOverviews(symbols)
            result?.let { _overviews.value = it }
        }
    }

    fun loadTimeSeries(symbols: List<String>) {
        viewModelScope.launch {
            val result = repository.getMultipleTimeSeries(symbols)
            result?.let { _timeSeries.value = it }
        }
    }
}

