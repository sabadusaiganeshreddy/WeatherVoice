package com.example.weathervoiceapp.utils

import com.example.weathervoiceapp.data.models.WeatherData
import java.text.SimpleDateFormat
import java.util.*

object WeatherSpeechGenerator {

    fun generateCurrentWeatherSpeech(weatherData: WeatherData): String {
        val current = weatherData.current
        val location = current.name
        val temperature = current.main.temp.toInt()
        val description = current.weather.firstOrNull()?.description ?: "unknown conditions"
        val feelsLike = current.main.feels_like.toInt()
        val humidity = current.main.humidity
        val windSpeed = current.wind.speed.toInt()

        return buildString {
            append("Good day! Here is the current weather for $location. ")
            append("The temperature is $temperature degrees Celsius. ")
            append("Weather conditions are $description. ")
            append("It feels like $feelsLike degrees. ")
            append("Humidity is $humidity percent. ")
            append("Wind speed is $windSpeed meters per second. ")

            // Add helpful advice based on conditions
            when {
                temperature > 35 -> append("It's very hot today. Stay hydrated and avoid direct sunlight. ")
                temperature < 10 -> append("It's quite cold. Wear warm clothes. ")
                humidity > 80 -> append("It's very humid today. ")
                windSpeed > 10 -> append("It's windy today. ")
            }

            if (current.weather.any { it.main.contains("rain", true) }) {
                append("Don't forget to carry an umbrella as it might rain. ")
            }
        }
    }

    fun generateForecastSpeech(weatherData: WeatherData): String {
        val forecast = weatherData.forecast
        val dailyForecasts = forecast.list.chunked(8).take(5) // Next 5 days

        return buildString {
            append("Here is the 5-day weather forecast. ")

            dailyForecasts.forEachIndexed { index, dayForecasts ->
                val dayForecast = dayForecasts.first()
                val date = Date(dayForecast.dt * 1000)
                val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
                val temp = dayForecast.main.temp.toInt()
                val description = dayForecast.weather.first().description
                val rainChance = (dayForecast.pop * 100).toInt()

                when (index) {
                    0 -> append("Today, ")
                    1 -> append("Tomorrow, ")
                    else -> append("On $dayName, ")
                }

                append("temperature will be $temp degrees with $description. ")

                if (rainChance > 30) {
                    append("There is a $rainChance percent chance of rain. ")
                }

                // Add farming-specific advice
                when {
                    rainChance > 70 -> append("Good day for indoor farm work. ")
                    rainChance in 30..70 -> append("Keep an eye on the sky. ")
                    temp > 30 && rainChance < 20 -> append("Good day for watering crops. ")
                    temp < 15 -> append("Protect sensitive plants from cold. ")
                }
            }

            append("That's your 5-day forecast. Plan your activities accordingly.")
        }
    }

    fun generateDetailedDaySpeech(weatherData: WeatherData, dayIndex: Int): String {
        val forecast = weatherData.forecast
        val dailyForecasts = forecast.list.chunked(8)

        if (dayIndex >= dailyForecasts.size) {
            return "Weather information not available for that day."
        }

        val dayForecast = dailyForecasts[dayIndex]
        val date = Date(dayForecast.first().dt * 1000)
        val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)

        // Morning, afternoon, evening forecasts
        val morning = dayForecast.getOrNull(0) // 6 AM
        val afternoon = dayForecast.getOrNull(4) // 6 PM
        val evening = dayForecast.getOrNull(7) // 9 PM

        return buildString {
            append("Detailed forecast for $dayName. ")

            morning?.let {
                append("In the morning, temperature will be ${it.main.temp.toInt()} degrees with ${it.weather.first().description}. ")
            }

            afternoon?.let {
                append("In the afternoon, ${it.main.temp.toInt()} degrees with ${it.weather.first().description}. ")
            }

            evening?.let {
                append("In the evening, ${it.main.temp.toInt()} degrees with ${it.weather.first().description}. ")
            }

            val maxTemp = dayForecast.maxOf { it.main.temp }.toInt()
            val minTemp = dayForecast.minOf { it.main.temp }.toInt()
            val avgHumidity = dayForecast.map { it.main.humidity }.average().toInt()

            append("Daily high will be $maxTemp degrees, low $minTemp degrees. ")
            append("Average humidity $avgHumidity percent. ")
        }
    }
}