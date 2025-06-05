package com.example.weathervoiceapp.utils

import com.example.weathervoiceapp.data.models.WeatherData

object FarmingAdvisor {

    fun getFarmingAdvice(weatherData: WeatherData): List<String> {
        val advice = mutableListOf<String>()
        val current = weatherData.current
        val forecast = weatherData.forecast

        // Temperature-based advice
        when {
            current.main.temp > 35 -> {
                advice.add("🌡️ Very hot day - Water crops early morning or evening")
                advice.add("🌿 Protect livestock from heat stress")
            }
            current.main.temp < 10 -> {
                advice.add("❄️ Cold weather - Cover sensitive crops")
                advice.add("🛡️ Protect young plants from frost")
            }
        }

        // Rain-based advice
        val rainToday = forecast.list.take(8).any { it.pop > 0.7 }
        val rainTomorrow = forecast.list.drop(8).take(8).any { it.pop > 0.7 }

        when {
            rainToday -> {
                advice.add("☔ Heavy rain expected - Postpone spraying")
                advice.add("🌾 Good day for indoor farm work")
            }
            rainTomorrow -> {
                advice.add("🌧️ Rain tomorrow - Complete outdoor work today")
            }
            !rainToday && current.main.humidity < 40 -> {
                advice.add("💧 Low humidity - Increase watering frequency")
            }
        }

        // Wind-based advice
        if (current.wind.speed > 10) {
            advice.add("💨 Windy conditions - Avoid pesticide spraying")
            advice.add("🌳 Secure loose materials in farm")
        }

        // Seasonal farming tips
        val month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)
        when (month) {
            0, 1 -> advice.add("🌱 Winter season - Good time for soil preparation")
            2, 3 -> advice.add("🌸 Spring season - Ideal for sowing summer crops")
            4, 5 -> advice.add("☀️ Summer season - Focus on irrigation management")
            6, 7, 8 -> advice.add("🌧️ Monsoon season - Monitor for pests and diseases")
            9, 10 -> advice.add("🍂 Post-monsoon - Good for harvesting kharif crops")
            11 -> advice.add("❄️ Winter prep - Time for rabi crop sowing")
        }

        return advice.take(3) // Limit to 3 most relevant tips
    }

    fun getWeatherAlert(weatherData: WeatherData): String? {
        val current = weatherData.current
        val forecast = weatherData.forecast

        // Extreme temperature alert
        if (current.main.temp > 40) {
            return "⚠️ HEAT WAVE ALERT: Temperature above 40°C. Take precautions!"
        }

        if (current.main.temp < 5) {
            return "❄️ COLD WAVE ALERT: Temperature below 5°C. Protect crops!"
        }

        // Heavy rain alert
        val heavyRainSoon = forecast.list.take(8).any { it.pop > 0.8 }
        if (heavyRainSoon) {
            return "🌧️ HEAVY RAIN ALERT: 80%+ chance of rain in next 24 hours!"
        }

        // High wind alert
        if (current.wind.speed > 15) {
            return "💨 WIND ALERT: Strong winds above 15 m/s. Secure farm materials!"
        }

        return null
    }
}