package com.moose.foodies.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.moose.foodies.local.ItemsDao
import com.moose.foodies.remote.ApiEndpoints
import com.moose.foodies.util.Preferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@HiltWorker
class ItemWorker @AssistedInject constructor(
    private val dao: ItemsDao,
    private val api: ApiEndpoints,
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val preferences: Preferences,
): CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val update = preferences.getUpdate()
            val time = SimpleDateFormat("hh:mm:ss", Locale.UK).format(Date())
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Date())

            dao.addItems(api.getItems(update))
            preferences.setUpdate("${date}T$time")

            Result.success()
        } catch (e: Throwable){
            Log.e("ItemWorker", "doWork: $e")
            Result.failure()
        }
    }
}