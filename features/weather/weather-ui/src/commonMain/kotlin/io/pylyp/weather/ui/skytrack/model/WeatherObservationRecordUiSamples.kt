package io.pylyp.weather.ui.skytrack.model

/** Preview and screenshot tests. */
internal fun sampleWeatherObservationRecordUi(): WeatherObservationRecordUi =
    WeatherObservationRecordUi(
        id = 1L,
        createdAtEpochMs = 1_700_000_000_000L,
        location = ObservationLocationUi.WithPlace(
            placeLabel = "Kyiv",
            coordinatesLine = "50.45°, 30.52°",
        ),
        userTemperatureC = 5.0,
        userHumidityPercent = 60,
        userPressureMmHg = 760,
        userWindDirection = WindDirectionUi.NORTH,
        userWindStrengthPercent = 50,
        userWeatherTypes = setOf(WeatherTypeUi.CLOUDY),
        apiTemperatureC = 3.0,
        apiHumidityPercent = 58,
        apiPressureMmHg = 758.0,
        apiWindDescription = "10 km/h NE",
        apiDescription = "Cloudy",
        temperatureDeltaC = 2.0,
        isHighDiscrepancy = false,
    )
