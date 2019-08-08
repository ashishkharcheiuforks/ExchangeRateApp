package com.dawid.currencies.viewmodels

import androidx.lifecycle.*
import com.dawid.currencies.domain.ExchangeRate
import com.dawid.currencies.domain.asChartEntry
import com.dawid.currencies.repository.CurrenciesRepository
import com.github.mikephil.charting.data.Entry
import javax.inject.Inject


class ExchangeRateDetailViewModel @Inject constructor(var currenciesRepository: CurrenciesRepository) : ViewModel() {

    private val _exchangeRate = MutableLiveData<ExchangeRate>()
    val exchangeRate: LiveData<ExchangeRate>
        get() = _exchangeRate

    lateinit var exchangeRateHistory: LiveData<List<ExchangeRate>>


    fun start(exchangeRate: ExchangeRate) {
        _exchangeRate.value = exchangeRate
        exchangeRateHistory = currenciesRepository.getAllRatesForCurrency(exchangeRate.currencyCode)
    }

    fun getChartData() : List<Entry> {
        return exchangeRateHistory.value?.asChartEntry()!!
    }

}