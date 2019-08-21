package com.dawid.currencies.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.dawid.currencies.domain.ExchangeRate
import com.dawid.currencies.repository.CurrenciesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class OverviewViewModel @Inject constructor(var currenciesRepository: CurrenciesRepository, sharedPreferences: SharedPreferences) : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
    private val _rates: LiveData<List<ExchangeRate>> = currenciesRepository.rates
    val rates: LiveData<List<ExchangeRate>>
        get() = _rates
    private val _navigateToSelectedExchangeRateDetails = MutableLiveData<ExchangeRate>()
    val navigateToSelectedExchangeRateDetails: LiveData<ExchangeRate>
        get() = _navigateToSelectedExchangeRateDetails

    init {
        val baseCurrency = sharedPreferences.getString("base_currency", "EUR")
        coroutineScope.launch {
            try {
                currenciesRepository.refreshData(baseCurrency)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun displayExchangeRateDetails(exchangeRate: ExchangeRate) {
        _navigateToSelectedExchangeRateDetails.value = exchangeRate
    }

    fun onDisplayExchangeRateDetailsCompleted() {
        _navigateToSelectedExchangeRateDetails.value = null
    }

}