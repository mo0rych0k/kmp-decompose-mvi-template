package io.pylyp.sample.composeapp.notification

import io.pylyp.common.resources.Res
import io.pylyp.common.resources.weather_notification_question_1
import io.pylyp.common.resources.weather_notification_question_2
import io.pylyp.common.resources.weather_notification_question_3
import io.pylyp.common.resources.weather_notification_question_4
import io.pylyp.common.resources.weather_notification_question_5
import io.pylyp.common.resources.weather_notification_question_6
import io.pylyp.common.resources.weather_notification_question_7
import kotlin.random.Random
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

/**
 * Localized weather check-in prompts from [common/resources]. [randomLocalized] picks a new line each time.
 */
public object WeatherNotificationQuestionTexts {

    private val questionResources: List<StringResource> =
        listOf(
            Res.string.weather_notification_question_1,
            Res.string.weather_notification_question_2,
            Res.string.weather_notification_question_3,
            Res.string.weather_notification_question_4,
            Res.string.weather_notification_question_5,
            Res.string.weather_notification_question_6,
            Res.string.weather_notification_question_7,
        )

    public suspend fun randomLocalized(random: Random = Random.Default): String =
        getString(questionResources.random(random))
}
