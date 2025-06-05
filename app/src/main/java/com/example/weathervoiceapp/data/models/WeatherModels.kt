package com.example.weathervoiceapp.data.models

// Current Weather Response (Free API)
data class CurrentWeatherResponse(
    val coord: Coordinates,
    val weather: List<WeatherCondition>,
    val base: String,
    val main: MainWeatherData,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

// 5-day forecast response (Free API)
data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItem>,
    val city: City
)

data class ForecastItem(
    val dt: Long,
    val main: MainWeatherData,
    val weather: List<WeatherCondition>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double, // Probability of precipitation
    val sys: ForecastSys,
    val dt_txt: String
)

data class MainWeatherData(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int? = null,
    val grnd_level: Int? = null
)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null
)

data class Clouds(
    val all: Int
)

data class Coordinates(
    val lon: Double,
    val lat: Double
)

data class Sys(
    val type: Int? = null,
    val id: Int? = null,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class ForecastSys(
    val pod: String
)

data class City(
    val id: Int,
    val name: String,
    val coord: Coordinates,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

// Combined data class for our app
data class WeatherData(
    val current: CurrentWeatherResponse,
    val forecast: ForecastResponse
)