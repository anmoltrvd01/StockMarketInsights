package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.roomdb.WatchlistEntity
import com.example.stockmarketinsights.roomdb.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WatchlistViewModel(
    private val repository: WatchlistRepository
) : ViewModel() {

    private val _watchlistItems = MutableStateFlow<List<WatchlistEntity>>(emptyList())
    val watchlistItems: StateFlow<List<WatchlistEntity>> =
        _watchlistItems.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllWatchlists().collectLatest { list ->
                _watchlistItems.value = list
            }
        }
    }


    val watchlistNames: StateFlow<List<String>> =
        MutableStateFlow(emptyList())

    fun addStockToWatchlist(
        watchlistName: String,
        stock: StockSummaryItem
    ) {
        viewModelScope.launch {
            val entity = WatchlistEntity(
                watchlistName = watchlistName,
                stockName = stock.name,
                stockSymbol = stock.symbol
            )
            repository.insertWatchlist(entity)
        }
    }

    fun deleteWatchlistItem(item: WatchlistEntity) {
        viewModelScope.launch {
            repository.deleteWatchlist(item)
        }
    }

    fun deleteWatchlistByName(name: String) {
        viewModelScope.launch {
            _watchlistItems.value
                .filter { it.watchlistName == name }
                .forEach { repository.deleteWatchlist(it) }
        }
    }

    fun renameWatchlist(oldName: String, newName: String) {
        viewModelScope.launch {
            repository.renameWatchlist(oldName, newName)
        }
    }
}

class WatchlistViewModelFactory(
    private val repository: WatchlistRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WatchlistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
