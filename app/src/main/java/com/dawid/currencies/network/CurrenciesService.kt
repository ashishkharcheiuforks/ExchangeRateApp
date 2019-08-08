package com.dawid.currencies.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CurrenciesService {
    @GET("/latest")
    fun getLatestExchangeRates(@Query("base") base: String = "EUR") : Deferred<CurrencyDTO>

    @GET("/history")
    fun getExchangeRatesBetween(
        @Query("start_at") startDate: String,
        @Query("end_at") endDate: String,
        @Query("base") base: String = "EUR"
    ) : Deferred<CurrenciesDTO>

    @GET("/{date}")
    fun getExchangeRatesForDate(
        @Path(value = "date") date: String,
        @Query("base") base: String = "EUR"
    ) : Deferred<CurrenciesDTO>
}