package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ExploreViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filterBy = MutableStateFlow("Name")
    val filterBy: StateFlow<String> = _filterBy

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun updateFilterBy(option: String) {
        _filterBy.value = option
    }

    val allGainers = listOf(
        StockSummaryItem("Apple Inc.", "AAPL", "$195.23", "+2.1%"),
        StockSummaryItem("Tesla Inc.", "TSLA", "$254.32", "+5.3%"),
        StockSummaryItem("Netflix", "NFLX", "$410.23", "+1.4%"),
        StockSummaryItem("Meta", "META", "$298.56", "+3.6%")
    )

    val allLosers = listOf(
        StockSummaryItem("Intel", "INTC", "$34.12", "-2.5%"),
        StockSummaryItem("Nvidia", "NVDA", "$430.89", "-0.9%"),
        StockSummaryItem("Google", "GOOG", "$135.67", "-1.3%"),
        StockSummaryItem("Amazon", "AMZN", "$118.45", "-4.6%")
    )
}
