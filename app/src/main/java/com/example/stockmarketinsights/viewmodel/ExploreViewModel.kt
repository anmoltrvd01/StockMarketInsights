package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.repository.StockRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ExploreViewModel(
    private val repository: StockRepository = StockRepository()
) : ViewModel() {

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<StockSummaryItem>>(emptyList())
    val searchResults: StateFlow<List<StockSummaryItem>> = _searchResults

    // Filter state
    private val _filterBy = MutableStateFlow("Name")
    val filterBy: StateFlow<String> = _filterBy

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isNotBlank()) {
            searchStocks(newQuery)
        } else {
            _searchResults.value = emptyList()
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun updateFilterBy(option: String) {
        _filterBy.value = option
    }

    // Gainers and Losers state
    private val _allGainers = MutableStateFlow<List<StockSummaryItem>>(emptyList())
    val allGainers: StateFlow<List<StockSummaryItem>> = _allGainers

    private val _allLosers = MutableStateFlow<List<StockSummaryItem>>(emptyList())
    val allLosers: StateFlow<List<StockSummaryItem>> = _allLosers

    // Keep track of active jobs so we can cancel them
    private var gainersJob: Job? = null
    private var losersJob: Job? = null

    init {
        refreshData()
    }

    //Refresh both gainers & losers safely
    fun refreshData() {
        // cancel any running jobs
        gainersJob?.cancel()
        losersJob?.cancel()

        gainersJob = fetchTopStocks("gainers")
        losersJob = fetchTopStocks("losers")
    }

    private fun fetchTopStocks(type: String): Job {
        return viewModelScope.launch {
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

    // Search API integration
    private var searchJob: Job? = null
    private fun searchStocks(query: String) {
        searchJob?.cancel() // cancel old search if new query comes in
        searchJob = viewModelScope.launch {
            try {
                val response = repository.searchSymbol(query)
                if (response.isSuccessful) {
                    val body = response.body() ?: "[]"
                    val jsonArray = JSONArray(body)
                    val stocks = mutableListOf<StockSummaryItem>()

                    for (i in 0 until jsonArray.length()) {
                        val obj: JSONObject = jsonArray.getJSONObject(i)
                        stocks.add(
                            StockSummaryItem(
                                name = obj.optString("name", "N/A"),
                                symbol = obj.optString("symbol", "N/A"),
                                price = obj.optString("price", "N/A"),
                                changePercent = obj.optString("changePercent", "N/A")
                            )
                        )
                    }
                    _searchResults.value = stocks
                } else {
                    println("Error searching stocks: ${response.errorBody()?.string()}")
                    _searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }
}
