package com.dawid.currencies.network

import com.dawid.currencies.database.DatabaseRate
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyDTO(
    val rates: Map<String, Double>,
    val base: String,
    val date: String)

@JsonClass(generateAdapter = true)
data class CurrenciesDTO(
    val rates: Map<String, Map<String, Double>>,
    val base: String)


/**
 * Helper functions to convert DTO coming from API to database savable objects
 */
fun CurrencyDTO.asDatabaseModel() : Array<DatabaseRate> {
    return rates.map {
        DatabaseRate(
            date = date,
            base = base,
            currencyCode = it.key,
            value = it.value
        )
    }.toTypedArray()
}

fun CurrenciesDTO.asDatabaseModel() : Array<DatabaseRate> {
    return rates.flatMap { rates ->
        rates.value.map {
            DatabaseRate(
                date = rates.key,
                base = base,
                currencyCode = it.key,
                value = it.value
            )
        }
    }.toTypedArray()
}