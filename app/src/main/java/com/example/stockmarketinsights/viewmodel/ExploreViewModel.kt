package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.dataModel.UiState
import com.example.stockmarketinsights.repository.StockRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val repository: StockRepository

) : ViewModel() {

    private var gainersPage = 1
    private var losersPage = 1
    private val pageSize = 20
    private val _isLoadingMoreGainers = MutableStateFlow(false)
    val isLoadingMoreGainers = _isLoadingMoreGainers.asStateFlow()

    private val _isLoadingMoreLosers = MutableStateFlow(false)
    val isLoadingMoreLosers = _isLoadingMoreLosers.asStateFlow()

    //SEARCH
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults =
        MutableStateFlow<UiState<List<StockSummaryItem>>>(UiState.Success(emptyList()))
    val searchResults: StateFlow<UiState<List<StockSummaryItem>>> = _searchResults

    private var searchJob: Job? = null

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery

        if (newQuery.isBlank()) {
            _searchResults.value = UiState.Success(emptyList())
            return
        }

        searchStocks(newQuery)
    }

    private fun searchStocks(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _searchResults.value = UiState.Loading
            try {
                val result = repository.searchSymbol(query)
                _searchResults.value = UiState.Success(result)
            } catch (e: Exception) {
                _searchResults.value = UiState.Error("Unable to fetch stocks")
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = UiState.Success(emptyList())
    }

    // FILTER
    private val _filterBy = MutableStateFlow("name")  // default filter
    val filterBy: StateFlow<String> = _filterBy

    fun updateFilterBy(newFilter: String) {
        _filterBy.value = newFilter
    }

    //STOCK DATA FOR SEARCH ALL
    private val _allGainers = MutableStateFlow<List<StockSummaryItem>>(emptyList())
    val allGainers: StateFlow<List<StockSummaryItem>> = _allGainers

    private val _allLosers = MutableStateFlow<List<StockSummaryItem>>(emptyList())
    val allLosers: StateFlow<List<StockSummaryItem>> = _allLosers

    fun fetchAllStocks() {
        viewModelScope.launch {
            try {
                println("FETCH ALL STOCKS CALLED")

                _allGainers.value = repository.getTopStocks("gainers")
                _allLosers.value = repository.getTopStocks("losers")

                println("GAINERS SIZE = ${_allGainers.value.size}")
                println("LOSERS SIZE = ${_allLosers.value.size}")

            } catch (e: Exception) {
                e.printStackTrace()
                _allGainers.value = emptyList()
                _allLosers.value = emptyList()
            }
        }
    }

    fun loadMoreGainers() {
        if (_isLoadingMoreGainers.value) return

        viewModelScope.launch {
            _isLoadingMoreGainers.value = true
            gainersPage++

            val all = repository.getTopStocks("gainers")
            val nextChunk = all.take(gainersPage * pageSize)

            _allGainers.value = nextChunk
            _isLoadingMoreGainers.value = false
        }
    }

    fun loadMoreLosers() {
        if (_isLoadingMoreLosers.value) return

        viewModelScope.launch {
            _isLoadingMoreLosers.value = true
            losersPage++

            val all = repository.getTopStocks("losers")
            val nextChunk = all.take(losersPage * pageSize)

            _allLosers.value = nextChunk
            _isLoadingMoreLosers.value = false
        }
    }



}
