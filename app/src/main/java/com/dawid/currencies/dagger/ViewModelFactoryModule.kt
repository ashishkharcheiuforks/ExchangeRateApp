package com.dawid.currencies.dagger

import androidx.lifecycle.ViewModelProvider
import com.dawid.currencies.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
// Abstract = no need to instantiate in application
internal abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}