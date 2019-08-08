package com.dawid.currencies.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dawid.currencies.repository.CurrenciesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class SettingsViewModel @Inject constructor(var sharedPreferences: SharedPreferences): ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
    @Inject lateinit var currenciesRepository: CurrenciesRepository


    fun refreshData() {
        val baseCurrency = sharedPreferences.getString("base_currency", "EUR")
        coroutineScope.launch {
            try {
                currenciesRepository.refreshData(baseCurrency)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

}