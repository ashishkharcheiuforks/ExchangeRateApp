package com.dawid.currencies.dagger

import android.app.Application
import androidx.room.Room
import com.dawid.currencies.CurrenciesApplication
import com.dawid.currencies.database.CurrenciesDatabase
import com.dawid.currencies.network.CurrenciesService
import com.dawid.currencies.repository.CurrenciesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) : CurrenciesDatabase =
        Room.databaseBuilder(
            application,
            CurrenciesDatabase::class.java,
            "currencies"
        ).build()

    @Provides
    @Singleton
    fun provideCurrenciesRepository(currenciesService: CurrenciesService, database: CurrenciesDatabase) : CurrenciesRepository =
        CurrenciesRepository(currenciesService, database)

}