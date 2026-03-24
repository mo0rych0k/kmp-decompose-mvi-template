package io.pylyp.sample.composeapp.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

public class WeatherDailyNotificationWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        WeatherNotificationAndroid.showWeatherQuestionNotification(applicationContext)
        WeatherNotificationAndroid.scheduleNextDailyNotification(applicationContext)
        return Result.success()
    }
}
