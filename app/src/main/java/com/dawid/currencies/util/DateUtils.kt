package com.dawid.currencies.util

import org.joda.time.DateTime
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat

fun convertStringtoDate(date: String, format: String = "yyyy-MM-dd"): DateTime {
    return DateTime.parse(date, DateTimeFormat.forPattern(format))
}

fun getDateFromFloat(value: Float): DateTime {
    val milis = value.toLong()
    return Instant(milis).toDateTime()
}

fun getDateString(daysBack: Int = 0) : String {
    val date = DateTime.now().minusDays(daysBack)
    return date.toString(DateTimeFormat.forPattern("yyyy-MM-dd"))
}