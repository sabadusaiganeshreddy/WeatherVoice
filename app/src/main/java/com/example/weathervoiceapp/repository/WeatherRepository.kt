package com.example.weathervoiceapp.repository

import com.example.weathervoiceapp.data.models.WeatherData
import com.example.weathervoiceapp.network.WeatherApiClient
import com.example.weathervoiceapp.utils.Constants
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class WeatherRepository {
    private val apiService = WeatherApiClient.weatherService

    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Result<WeatherData> {
        return try {
            coroutineScope {
                // Make both API calls concurrently
                val currentWeatherDeferred = async {
                    apiService.getCurrentWeather(
                        latitude = latitude,
                        longitude = longitude,
                        apiKey = Constants.WEATHER_API_KEY
                    )
                }

                val forecastDeferred = async {
                    apiService.getForecast(
                        latitude = latitude,
                        longitude = longitude,
                        apiKey = Constants.WEATHER_API_KEY
                    )
                }

                val currentResponse = currentWeatherDeferred.await()
                val forecastResponse = forecastDeferred.await()

                if (currentResponse.isSuccessful && forecastResponse.isSuccessful &&
                    currentResponse.body() != null && forecastResponse.body() != null) {

                    val weatherData = WeatherData(
                        current = currentResponse.body()!!,
                        forecast = forecastResponse.body()!!
                    )

                    Result.success(weatherData)
                } else {
                    val errorMsg = when {
                        !currentResponse.isSuccessful -> "Current weather error: ${currentResponse.message()}"
                        !forecastResponse.isSuccessful -> "Forecast error: ${forecastResponse.message()}"
                        else -> "Unknown error occurred"
                    }
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}