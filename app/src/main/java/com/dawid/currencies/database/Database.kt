package com.dawid.currencies.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurrencyRateDao {
    @Query("select * from currency_rate")
    fun getAll() : LiveData<List<DatabaseRate>>

    @Query("select r.*, r.value - (select value from currency_rate where r.curr_code = curr_code and (julianday(r.date) - julianday(date) = 1)) as diff from currency_rate r where r.date = (select max(date) from currency_rate)")
    fun getIt() : LiveData<List<DatabaseRateWithDiff>>

    @Query("select * from currency_rate where curr_code = :currCode and date = (select max(date) from currency_rate where curr_code = :currCode)")
    fun getLatestExchangeRate(currCode: String) : LiveData<DatabaseRate>

    @Query("select * from currency_rate where date = :date order by curr_code ASC")
    fun getAllRatesForDate(date: String) : LiveData<List<DatabaseRate>>

    @Query("select * from currency_rate where curr_code = :currCode order by date DESC")
    fun getAllRatesForCurrency(currCode: String) : LiveData<List<DatabaseRate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseRate)

    @Query("delete from currency_rate")
    fun clearAll()

}

@Database(entities = [DatabaseRate::class], version = 1, exportSchema = false)
abstract class CurrenciesDatabase : RoomDatabase() {
    abstract val currencyRateDao: CurrencyRateDao
}
