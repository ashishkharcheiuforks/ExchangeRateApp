package com.dawid.currencies.dagger

import com.dawid.currencies.dagger.viewmodel.ViewModelModule
import com.dawid.currencies.ui.CalculatorDialogFragment
import com.dawid.currencies.ui.ExchangeRateDetailFragment
import com.dawid.currencies.ui.ExchangeRatesOverviewFragment
import com.dawid.currencies.ui.SettingsFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    ViewModelFactoryModule::class,
    ViewModelModule::class
])
interface AppComponent {
    //provide injection targets
    //fun inject(target: CurrenciesApplication)
    fun inject(target: ExchangeRatesOverviewFragment)
    fun inject(target: ExchangeRateDetailFragment)
    fun inject(target: CalculatorDialogFragment)
    fun inject(target: SettingsFragment)

}
