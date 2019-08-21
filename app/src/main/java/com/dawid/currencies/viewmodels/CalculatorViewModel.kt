package com.dawid.currencies.viewmodels

import androidx.lifecycle.*
import com.dawid.currencies.domain.ExchangeRate
import com.dawid.currencies.repository.CurrenciesRepository
import javax.inject.Inject

class CalculatorViewModel @Inject constructor(var repository: CurrenciesRepository) : ViewModel() {

    val currency = MutableLiveData<String>()
    var amount: Double = 0.0

    val exchangeRate: LiveData<ExchangeRate> = Transformations.switchMap(currency) {
        getLatestRateForCurrency(it)
    }
    
    private val _result: MutableLiveData<Double> = MutableLiveData()
    val result: LiveData<Double>
        get() = _result


    init {
        currency.value = "CAD"
        _result.value = 0.0
    }

    fun calculateResult() {
        exchangeRate.value?.let {
            _result.value = amount / it.value
        }
    }

    private fun getLatestRateForCurrency(currency: String) : LiveData<ExchangeRate> {
        return repository.getLatestRateForCurrency(currency)
    }

}