package com.dawid.currencies.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dawid.currencies.repository.CurrenciesRepository
import retrofit2.HttpException
import javax.inject.Inject

class RefreshDataWorker(val context: Context, val params: WorkerParameters) : CoroutineWorker(context, params) {

    @Inject lateinit var currenciesRepository: CurrenciesRepository

    override suspend fun doWork(): Result {
        return try {
            currenciesRepository.refreshData()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}