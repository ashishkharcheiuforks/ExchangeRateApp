package com.dawid.currencies

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import androidx.preference.PreferenceManager
import androidx.work.*
import com.dawid.currencies.dagger.*
import com.dawid.currencies.work.RefreshDataWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit


class CurrenciesApplication : Application() {

    private lateinit var sharedPreferences: SharedPreferences
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        Timber.plant(Timber.DebugTree())
        initializeIfNecessary()
    }

    private fun initDagger(app: CurrenciesApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .networkModule(NetworkModule())
            .databaseModule(DatabaseModule())
            .build()

    fun scheduleWork(intervalHours: Double = 8.0) {
        Timber.i("SCHEDULED: $intervalHours")
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .apply {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()
        val reccuringWork: PeriodicWorkRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(intervalHours.toLong(), TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            reccuringWork
        )
    }

    private fun initializeIfNecessary() {
        val sharedPreferencesEditor = sharedPreferences.edit()
        if(!sharedPreferences.contains("base_currency")) {
            sharedPreferencesEditor.putString("base_currency", "EUR")
        }
        if (!sharedPreferences.contains("refresh_rate")) {
            sharedPreferencesEditor.putString("refresh_rate", "8.0")
            scheduleWork()
        }
        sharedPreferencesEditor.apply()
    }

}