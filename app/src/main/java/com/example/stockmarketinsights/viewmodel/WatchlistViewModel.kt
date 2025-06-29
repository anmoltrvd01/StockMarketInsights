package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketinsights.roomdb.WatchlistEntity
import com.example.stockmarketinsights.roomdb.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WatchlistViewModel(private val repository: WatchlistRepository) : ViewModel() {

    private val _watchlistItems = MutableStateFlow<List<WatchlistEntity>>(emptyList())
    val watchlistItems: StateFlow<List<WatchlistEntity>> = _watchlistItems

    init {
        loadWatchlist()
    }

    private fun loadWatchlist() {
        viewModelScope.launch {
            repository.getAllWatchlistItems().collect {
                _watchlistItems.value = it
            }
        }
    }

    fun addToWatchlist(item: WatchlistEntity) {
        viewModelScope.launch {
            repository.insertStock(item)
        }
    }

    fun removeFromWatchlist(item: WatchlistEntity) {
        viewModelScope.launch {
            repository.deleteStock(item)
        }
    }
}
