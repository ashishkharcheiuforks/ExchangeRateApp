package com.dawid.currencies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.dawid.currencies.database.CurrenciesDatabase
import com.dawid.currencies.database.asDomainModel
import com.dawid.currencies.domain.ExchangeRate
import com.dawid.currencies.network.CurrenciesService
import com.dawid.currencies.network.asDatabaseModel
import com.dawid.currencies.util.getDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CurrenciesRepository @Inject constructor(var currenciesService: CurrenciesService, var database: CurrenciesDatabase) {

    val rates: LiveData<List<ExchangeRate>> =
        Transformations.map(database.currencyRateDao.getIt()) { it.asDomainModel() }


    fun getAllRatesForCurrency(currCode: String) : LiveData<List<ExchangeRate>> {
        return Transformations.map(database.currencyRateDao.getAllRatesForCurrency(currCode)) { it.asDomainModel() }
    }

    fun getLatestRateForCurrency(currCode: String) : LiveData<ExchangeRate> {
        return Transformations.map(database.currencyRateDao.getLatestExchangeRate(currCode)) { it.asDomainModel() }
    }

    suspend fun refreshData(baseCurrency: String = "EUR") {
        withContext(Dispatchers.IO) {
            database.currencyRateDao.clearAll()
            val rates = currenciesService.getExchangeRatesBetween(
                getDateString(7),
                getDateString(),
                baseCurrency
            ).await()
            database.currencyRateDao.insertAll(*rates.asDatabaseModel())
        }
    }

    suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            database.currencyRateDao.clearAll()
        }
    }

}