package com.dawid.currencies.database

import androidx.room.*
import com.dawid.currencies.domain.ExchangeRate

@Entity(tableName = "currency_rate", primaryKeys = ["curr_code", "date"])
data class DatabaseRate(
    @ColumnInfo(name = "curr_code")
    val currencyCode: String,
    @TypeConverters(DateConverter::class)
    val date: String,
    @ColumnInfo(name = "base_curr")
    val base: String,
    val value: Double
)

fun DatabaseRate.asDomainModel() : ExchangeRate {
    return ExchangeRate(
        currencyCode = currencyCode,
        date = date,
        base = base,
        value = value
    )
}


fun List<DatabaseRate>.asDomainModel() : List<ExchangeRate> {
    return map { it.asDomainModel() }
}



