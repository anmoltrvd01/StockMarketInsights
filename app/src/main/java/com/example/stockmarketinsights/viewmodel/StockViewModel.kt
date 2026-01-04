package com.example.stockmarketinsights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketinsights.network.CompanyOverview
import com.example.stockmarketinsights.network.DailySeries
import com.example.stockmarketinsights.repository.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StockViewModel(
    private val repository: StockRepository = StockRepository()
) : ViewModel() {

    // ---------------- Company Overview ----------------

    private val _companyOverview = MutableStateFlow<CompanyOverview?>(null)
    val companyOverview: StateFlow<CompanyOverview?> = _companyOverview

    // ---------------- Stock Price Chart ----------------

    private val _dailyPrices = MutableStateFlow<List<DailySeries>>(emptyList())
    val dailyPrices: StateFlow<List<DailySeries>> = _dailyPrices

    // ---------------- API Calls ----------------

    fun loadCompanyDetails(symbol: String) {
        viewModelScope.launch {
            try {
                _companyOverview.value = repository.getCompanyOverview(symbol)
            } catch (e: Exception) {
                e.printStackTrace()
                _companyOverview.value = null
            }
        }
    }

    fun loadDailyPrices(symbol: String) {
        viewModelScope.launch {
            try {
                _dailyPrices.value = repository.getDailyPrices(symbol)
            } catch (e: Exception) {
                e.printStackTrace()
                _dailyPrices.value = emptyList()
            }
        }
    }
}
