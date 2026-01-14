package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketinsights.dataModel.UiStockItem
import com.example.stockmarketinsights.dataModel.UiState
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

    // Search results wrapped in UiState
    private val _searchResults =
        MutableStateFlow<UiState<List<UiStockItem>>>(UiState.Success(emptyList()))
    val searchResults: StateFlow<UiState<List<UiStockItem>>> = _searchResults

    private var searchJob: Job? = null

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery

        if (newQuery.isBlank()) {
            _searchResults.value = UiState.Success(emptyList())
            return
        }

        searchStocks(newQuery)
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = UiState.Success(emptyList())
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
}
