package com.example.stockmarketinsights.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.network.RetrofitInstance
import com.example.stockmarketinsights.roomdb.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.stockmarketinsights.BuildConfig

class ExploreViewModel(
    private val repository: WatchlistRepository
) : ViewModel() {

    private val _topGainers = MutableStateFlow<List<StockSummaryItem>>(emptyList())
    val topGainers: StateFlow<List<StockSummaryItem>> = _topGainers

    private val _topLosers = MutableStateFlow<List<StockSummaryItem>>(emptyList())
    val topLosers: StateFlow<List<StockSummaryItem>> = _topLosers

    // Called once to check & load data
    fun initData() {
        viewModelScope.launch {
            val cachedGainers = repository.getAllGainers().firstOrNull()
            val cachedLosers = repository.getAllLosers().firstOrNull()

            if (cachedGainers.isNullOrEmpty() || cachedLosers.isNullOrEmpty()) {
                fetchTopGainers()
                fetchTopLosers()
            } else {
                loadGainersFromCache()
                loadLosersFromCache()
            }
        }
    }

    fun fetchTopGainers() {

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTopGainers(BuildConfig.ALPHA_VANTAGE_API_KEY)

                if (response.isSuccessful) {
                    val body = response.body()
                    val dataArray = body?.getAsJsonArray("top_gainers")

                    val gainers = dataArray?.mapNotNull { item ->
                        val obj = item.asJsonObject
                        val changePercent = obj["change_percentage"].asString.removeSuffix("%").toFloatOrNull()
                        val price = obj["price"].asString.toFloatOrNull()
                        if (changePercent != null && price != null && changePercent > 0) {
                            StockSummaryItem(
                                symbol = obj["ticker"].asString,
                                name = "",
                                price = price,
                                changePercent = changePercent
                            )
                        } else null
                    } ?: emptyList()

                    _topGainers.value = gainers

                    val cached = gainers.map {
                        GainerEntity(it.symbol, it.name, it.price, it.changePercent)
                    }
                    repository.cacheGainers(cached)
                } else {
                    Log.e("ExploreVM", "API limit hit: ${response.errorBody()?.string()}")
                    loadGainersFromCache()
                }
            } catch (e: Exception) {
                Log.e("ExploreVM", "Gainers Error: ${e.localizedMessage}")
                loadGainersFromCache()
            }
        }
    }

    fun fetchTopLosers() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTopLosers(BuildConfig.ALPHA_VANTAGE_API_KEY)

                if (response.isSuccessful) {
                    val body = response.body()
                    val dataArray = body?.getAsJsonArray("most_actively_traded")

                    val losers = dataArray?.mapNotNull { item ->
                        val obj = item.asJsonObject
                        val changePercent = obj["change_percentage"].asString.removeSuffix("%").toFloatOrNull()
                        val price = obj["price"].asString.toFloatOrNull()
                        if (changePercent != null && price != null && changePercent < 0) {
                            StockSummaryItem(
                                symbol = obj["ticker"].asString,
                                name = "",
                                price = price,
                                changePercent = changePercent
                            )
                        } else null
                    } ?: emptyList()

                    _topLosers.value = losers

                    val cached = losers.map {
                        LoserEntity(it.symbol, it.name, it.price, it.changePercent)
                    }
                    repository.cacheLosers(cached)
                } else {
                    Log.e("ExploreVM", "API limit hit: ${response.errorBody()?.string()}")
                    loadLosersFromCache()
                }
            } catch (e: Exception) {
                Log.e("ExploreVM", "Losers Error: ${e.localizedMessage}")
                loadLosersFromCache()
            }
        }
    }

    private fun loadGainersFromCache() {
        viewModelScope.launch {
            repository.getAllGainers().collect { cachedList ->
                _topGainers.value = cachedList.map {
                    StockSummaryItem(it.symbol, it.name, it.price, it.changePercent)
                }
            }
        }
    }

    private fun loadLosersFromCache() {
        viewModelScope.launch {
            repository.getAllLosers().collect { cachedList ->
                _topLosers.value = cachedList.map {
                    StockSummaryItem(it.symbol, it.name, it.price, it.changePercent)
                }
            }
        }
    }
}
