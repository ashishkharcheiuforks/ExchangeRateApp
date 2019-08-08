package com.dawid.currencies.database

import androidx.room.Embedded
import com.dawid.currencies.domain.ExchangeRate
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Instant
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

data class DatabaseRateWithDiff(
    @Embedded
    val databaseRate: DatabaseRate,
    val diff: Double
)

fun DatabaseRateWithDiff.asDomainModel() : ExchangeRate {
    return ExchangeRate(
        currencyCode = databaseRate.currencyCode,
        date = databaseRate.date,
        base = databaseRate.base,
        value = databaseRate.value,
        diff = diff
    )
}

fun List<DatabaseRateWithDiff>.asDomainModel() : List<ExchangeRate> {
    return map { it.asDomainModel() }
}