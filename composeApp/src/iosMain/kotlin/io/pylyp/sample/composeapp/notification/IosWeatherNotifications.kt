@file:OptIn(kotlin.time.ExperimentalTime::class)

package io.pylyp.sample.composeapp.notification

import io.pylyp.common.resources.Res
import io.pylyp.common.resources.weather_notification_title
import kotlin.time.Clock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import platform.Foundation.NSUserDefaults
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

/**
 * iOS shell: call [onAppLaunch] once after Koin bootstrap (e.g. from Swift `App.init`).
 * Schedules the next local notification at a random time between 10:00 and 18:00; reschedules each launch when authorized.
 */
public object IosWeatherNotifications {

    private const val PREFS_KEY = "io.pylyp.sample.weather_notifications.launch_count"
    private const val NOTIFICATION_ID = "weather_daily_question_reminder"

    public fun onAppLaunch() {
        val defaults = NSUserDefaults.standardUserDefaults
        val previous = defaults.integerForKey(PREFS_KEY).toInt()
        val nextCount = previous + 1
        defaults.setInteger(nextCount.toLong(), forKey = PREFS_KEY)

        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.getNotificationSettingsWithCompletionHandler { settings ->
            if (settings == null) {
                return@getNotificationSettingsWithCompletionHandler
            }

            val authorized = settings.authorizationStatus == UNAuthorizationStatusAuthorized
            if (authorized) {
                scheduleNextNotification()
                return@getNotificationSettingsWithCompletionHandler
            }

            if (nextCount % 3 != 0) {
                return@getNotificationSettingsWithCompletionHandler
            }

            val options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
            center.requestAuthorizationWithOptions(options) { granted, _ ->
                if (granted) {
                    scheduleNextNotification()
                }
            }
        }
    }

    private fun scheduleNextNotification() {
        val nextInstant = computeNextWeatherNotificationInstant()
        val delayMs =
            millisUntil(
                target = nextInstant,
                now = Clock.System.now(),
            )
        val intervalSeconds = (delayMs.coerceAtLeast(1_000L)).toDouble() / 1_000.0

        val (title, body) =
            runBlocking(Dispatchers.Default) {
                getString(Res.string.weather_notification_title) to WeatherNotificationQuestionTexts.randomLocalized()
            }

        val content = UNMutableNotificationContent()
        content.setTitle(title)
        content.setBody(body)

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(intervalSeconds, repeats = false)
        val request =
            UNNotificationRequest.requestWithIdentifier(
                NOTIFICATION_ID,
                content,
                trigger,
            )

        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.removePendingNotificationRequestsWithIdentifiers(listOf(NOTIFICATION_ID))
        center.addNotificationRequest(request) { _ -> }
    }
}
