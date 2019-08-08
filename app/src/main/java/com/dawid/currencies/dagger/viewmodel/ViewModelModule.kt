package com.dawid.currencies.dagger.viewmodel

import androidx.lifecycle.ViewModel
import com.dawid.currencies.ui.ExchangeRateDetailFragment
import com.dawid.currencies.ui.ExchangeRatesOverviewFragment
import com.dawid.currencies.ui.SettingsFragment
import com.dawid.currencies.viewmodels.CalculatorViewModel
import com.dawid.currencies.viewmodels.ExchangeRateDetailViewModel
import com.dawid.currencies.viewmodels.OverviewViewModel
import com.dawid.currencies.viewmodels.SettingsViewModel
import dagger.multibindings.IntoMap
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel::class)
    abstract fun bindOverviewViewModel(viewModel: OverviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExchangeRateDetailViewModel::class)
    abstract fun bindDetailViewModel(viewModel: ExchangeRateDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalculatorViewModel::class)
    abstract fun bindCalculatorViewModel(viewModel: CalculatorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel


    @ContributesAndroidInjector
    abstract fun contributeExchangeRatesOverviewFragment(): ExchangeRatesOverviewFragment

    @ContributesAndroidInjector
    abstract fun contributeExchangeRatesDetailFragment(): ExchangeRateDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeCalculatorFragment(): CalculatorViewModel

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

}