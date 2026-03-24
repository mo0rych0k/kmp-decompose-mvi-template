package io.pylyp.sample.composeapp.notification

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.weather_notification_title
import java.util.concurrent.TimeUnit
import org.jetbrains.compose.resources.getString
import androidx.core.content.edit

public object WeatherNotificationAndroid {

    private const val PREFS_NAME = "io.pylyp.sample.weather_notifications"
    private const val KEY_LAUNCH_COUNT = "launch_count"

    internal const val CHANNEL_ID: String = "weather_question_reminders"
    private const val NOTIFICATION_ID: Int = 9_001
    private const val UNIQUE_WORK_NAME: String = "weather_daily_question_reminder"

    public fun ensureNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                "Weather check-ins",
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = "Reminders to notice the current weather"
            }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    public fun onMainActivityLaunch(
        activity: ComponentActivity,
        permissionLauncher: ActivityResultLauncher<String>,
    ) {
        val prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val nextCount = prefs.getInt(KEY_LAUNCH_COUNT, 0) + 1
        prefs.edit { putInt(KEY_LAUNCH_COUNT, nextCount) }

        if (Build.VERSION.SDK_INT >= 33 && !hasNotificationPermission(activity)) {
            if (nextCount % 3 == 0) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            return
        }

        if (!hasNotificationPermission(activity)) {
            return
        }

        ensureDailyWeatherScheduled(activity.applicationContext)
    }

    public fun ensureDailyWeatherScheduled(context: Context) {
        if (!hasNotificationPermission(context)) {
            return
        }
        scheduleNextDailyNotification(context.applicationContext)
    }

    internal fun scheduleNextDailyNotification(context: Context) {
        val applicationContext = context.applicationContext
        val next = computeNextWeatherNotificationInstant()
        val delayMs = millisUntil(next)

        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

        val request =
            OneTimeWorkRequestBuilder<WeatherDailyNotificationWorker>()
                .setConstraints(constraints)
                .setInitialDelay(delayMs.coerceAtLeast(0L), TimeUnit.MILLISECONDS)
                .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniqueWork(
                UNIQUE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                request,
            )
    }

    @SuppressLint("MissingPermission")
    internal suspend fun showWeatherQuestionNotification(context: Context) {
        if (!hasNotificationPermission(context)) {
            return
        }

        ensureNotificationChannel(context)

        val title = getString(Res.string.weather_notification_title)
        val body = WeatherNotificationQuestionTexts.randomLocalized()
        val notification =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_compass)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    @SuppressLint("InlinedApi")
    public fun hasNotificationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= 33) {
            val granted =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                return false
            }
        }
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}
