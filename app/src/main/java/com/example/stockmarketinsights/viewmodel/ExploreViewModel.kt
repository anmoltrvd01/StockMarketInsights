package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketinsights.dataModel.UiStockItem
import com.example.stockmarketinsights.repository.StockRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val repository: StockRepository = StockRepository()
) : ViewModel() {

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Search results (UI model only)
    private val _searchResults = MutableStateFlow<List<UiStockItem>>(emptyList())
    val searchResults: StateFlow<List<UiStockItem>> = _searchResults

    private var searchJob: Job? = null

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery

        if (newQuery.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        searchStocks(newQuery)
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    private fun searchStocks(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                _searchResults.value = repository.searchStocks(query)
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }
}
