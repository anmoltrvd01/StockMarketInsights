package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.repository.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val repository: StockRepository = StockRepository()
) : ViewModel() {

    // Search & filter state
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

    // Gainers and Losers state
    private val _allGainers = MutableStateFlow<List<StockSummaryItem>>(emptyList())
    val allGainers: StateFlow<List<StockSummaryItem>> = _allGainers

    private val _allLosers = MutableStateFlow<List<StockSummaryItem>>(emptyList())
    val allLosers: StateFlow<List<StockSummaryItem>> = _allLosers

    init {
        fetchTopStocks("gainers")
        fetchTopStocks("losers")
    }

    private fun fetchTopStocks(type: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTopStocks(type)
                if (response.isSuccessful) {
                    val rawData = response.body() ?: emptyList()
                    val stocks = rawData.map { map ->
                        StockSummaryItem(
                            name = map["name"] ?: "N/A",
                            symbol = map["symbol"] ?: "N/A",
                            price = map["price"] ?: "N/A",
                            changePercent = map["changePercent"] ?: "N/A"
                        )
                    }
                    if (type == "gainers") {
                        _allGainers.value = stocks
                    } else {
                        _allLosers.value = stocks
                    }
                } else {
                    println("Error fetching $type: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
