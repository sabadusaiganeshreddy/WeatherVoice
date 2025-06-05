package com.example.weathervoiceapp
import androidx.compose.material.icons.filled.Language
import android.os.Bundle
import java.util.Calendar
import androidx.compose.material.icons.filled.Agriculture
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weathervoiceapp.data.models.WeatherData
import com.example.weathervoiceapp.ui.components.LocationPermissionHandler
import com.example.weathervoiceapp.ui.components.WeatherIcon
import com.example.weathervoiceapp.ui.theme.WeatherVoiceAppTheme
import com.example.weathervoiceapp.utils.FarmingAdvisor
import com.example.weathervoiceapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.example.weathervoiceapp.ui.components.LanguageSettingsDialog
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherVoiceAppTheme {
                WeatherApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(viewModel: WeatherViewModel = viewModel()) {
    var isVoiceMode by remember { mutableStateOf(false) }
    var showLocationPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Initialize services
    LaunchedEffect(Unit) {
        viewModel.initializeServices(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isVoiceMode) "Voice Weather" else "Weather App")
                },
                actions = {
                    // Location button
                    IconButton(
                        onClick = { showLocationPermission = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Get Current Location"
                        )
                    }

                    // Mode toggle button
                    IconButton(
                        onClick = { isVoiceMode = !isVoiceMode }
                    ) {
                        Icon(
                            imageVector = if (isVoiceMode) Icons.Default.Dashboard else Icons.Default.Mic,
                            contentDescription = if (isVoiceMode) "Switch to Normal Mode" else "Switch to Voice Mode"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Footer with your name
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Developed with ❤️ by Sai Ganesh Reddy",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Weather Voice App • 2025",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isVoiceMode) {
                VoiceModeScreen(viewModel = viewModel)
            } else {
                NormalModeScreen(viewModel = viewModel)
            }
        }
    }

    // Handle location permission
    if (showLocationPermission) {
        LocationPermissionHandler(
            onPermissionGranted = {
                showLocationPermission = false
                viewModel.loadCurrentLocationWeather()
            },
            onPermissionDenied = {
                showLocationPermission = false
            }
        )
    }
}

@Composable
fun NormalModeScreen(viewModel: WeatherViewModel) {
    val weatherData by viewModel.weatherData
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val locationEnabled by viewModel.locationEnabled

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Location status indicator
        if (locationEnabled) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Active",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Using your current location",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        when {
            isLoading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading weather data...")
            }
            error != null -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = error!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { viewModel.refreshWeather() },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
            weatherData != null -> {
                WeatherCard(weatherData!!, viewModel)
            }
        }
    }
}

@Composable
fun WeatherCard(weather: WeatherData, viewModel: WeatherViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Location and refresh button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = weather.current.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(
                    onClick = { viewModel.refreshWeather() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Weather"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Current weather with icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WeatherIcon(
                    weatherMain = weather.current.weather.firstOrNull()?.main ?: "Clear",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${weather.current.main.temp.toInt()}°C",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = weather.current.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "N/A",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weather details row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetail("Feels like", "${weather.current.main.feels_like.toInt()}°C")
                WeatherDetail("Humidity", "${weather.current.main.humidity}%")
                WeatherDetail("Wind", "${weather.current.wind.speed.toInt()} m/s")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Weather alerts
            val weatherAlert = FarmingAdvisor.getWeatherAlert(weather)
            if (weatherAlert != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = weatherAlert,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Forecast section
            Text(
                text = "5-Day Forecast",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Process forecast data to get daily summaries
            val dailyForecasts = processDailyForecasts(weather.forecast.list)

            dailyForecasts.forEachIndexed { index, dailyForecast ->
                ForecastItem(
                    dailyForecast = dailyForecast,
                    isToday = index == 0
                )
                if (index < dailyForecasts.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Replace the farming tips section in WeatherCard with this:
            Spacer(modifier = Modifier.height(16.dp))

// Farming tips section
            val farmingTips = FarmingAdvisor.getFarmingAdvice(weather)
        }
    }
}
// Add this data class for processed daily forecast
data class ProcessedDailyForecast(
    val date: Date,
    val minTemp: Double,
    val maxTemp: Double,
    val avgTemp: Double,
    val weatherMain: String,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val rainProbability: Double
)

// Helper function to process hourly forecasts into daily summaries
fun processDailyForecasts(forecastList: List<com.example.weathervoiceapp.data.models.ForecastItem>): List<ProcessedDailyForecast> {
    val calendar = Calendar.getInstance()
    val dailyGroups = mutableMapOf<String, MutableList<com.example.weathervoiceapp.data.models.ForecastItem>>()

    // Group forecasts by date
    forecastList.forEach { forecast ->
        calendar.timeInMillis = forecast.dt * 1000
        val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        if (dailyGroups[dateKey] == null) {
            dailyGroups[dateKey] = mutableListOf()
        }
        dailyGroups[dateKey]?.add(forecast)
    }

    // Process each day's forecasts
    return dailyGroups.entries.take(5).map { (dateKey, dayForecasts) ->
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateKey) ?: Date()

        val temperatures = dayForecasts.map { it.main.temp }
        val minTemp = temperatures.minOrNull() ?: 0.0
        val maxTemp = temperatures.maxOrNull() ?: 0.0
        val avgTemp = temperatures.average()

        // Get the most common weather condition for the day
        val weatherConditions = dayForecasts.map { it.weather.first().main }
        val mostCommonWeather = weatherConditions.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: "Clear"

        // Get description for the most common weather
        val description = dayForecasts.first { it.weather.first().main == mostCommonWeather }.weather.first().description

        val avgHumidity = dayForecasts.map { it.main.humidity }.average().toInt()
        val avgWindSpeed = dayForecasts.map { it.wind.speed }.average()
        val maxRainProb = dayForecasts.maxOfOrNull { it.pop } ?: 0.0

        ProcessedDailyForecast(
            date = date,
            minTemp = minTemp,
            maxTemp = maxTemp,
            avgTemp = avgTemp,
            weatherMain = mostCommonWeather,
            description = description,
            humidity = avgHumidity,
            windSpeed = avgWindSpeed,
            rainProbability = maxRainProb
        )
    }
}
@Composable
fun ForecastItem(
    dailyForecast: ProcessedDailyForecast,
    isToday: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Day name
        Text(
            text = if (isToday) "Today" else SimpleDateFormat("EEE", Locale.getDefault()).format(dailyForecast.date),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )

        // Weather icon
        WeatherIcon(
            weatherMain = dailyForecast.weatherMain,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Weather description
        Text(
            text = dailyForecast.description.replaceFirstChar { it.uppercase() },
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Temperature range
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${dailyForecast.maxTemp.toInt()}°C",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${dailyForecast.minTemp.toInt()}°C",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Rain chance if significant
        if (dailyForecast.rainProbability > 0.3) {
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = "Rain chance",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${(dailyForecast.rainProbability * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun WeatherDetail(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun VoiceModeScreen(viewModel: WeatherViewModel) {
    val isTtsReady by viewModel.isTtsReady
    val isSpeaking by viewModel.isSpeaking
    val weatherData by viewModel.weatherData
    val isLoading by viewModel.isLoading
    val availableLanguages by viewModel.availableLanguages
    val currentLanguage by viewModel.currentLanguage

    var showLanguageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Language selector at the top
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🗣️ Voice Weather Assistant",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { showLanguageDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language Settings"
                        )
                    }
                }

                // Current language indicator
                currentLanguage?.let { language ->
                    Text(
                        text = "Language: ${language.nativeName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Voice icon with visual feedback
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Mode",
                    modifier = Modifier.size(80.dp),
                    tint = if (isSpeaking) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Status text
                Text(
                    text = when {
                        isSpeaking -> "🔊 Speaking..."
                        !isTtsReady -> "⏳ Initializing voice..."
                        isLoading -> "📡 Loading weather..."
                        weatherData == null -> "❌ No weather data available"
                        else -> "✅ Ready to speak weather"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Current weather button
                Button(
                    onClick = {
                        if (isSpeaking) {
                            viewModel.stopSpeaking()
                        } else {
                            viewModel.speakCurrentWeather()
                        }
                    },
                    enabled = isTtsReady && weatherData != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSpeaking) "Stop Speaking" else "🌤️ Speak Today's Weather",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5-Day Forecast button
                Button(
                    onClick = {
                        if (isSpeaking) {
                            viewModel.stopSpeaking()
                        } else {
                            viewModel.speakForecast()
                        }
                    },
                    enabled = isTtsReady && weatherData != null && !isSpeaking,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📅 Speak 5-Day Forecast",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                // Stop button when speaking
                if (isSpeaking) {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { viewModel.stopSpeaking() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("⏹️ Stop")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Language settings dialog
    LanguageSettingsDialog(
        showDialog = showLanguageDialog,
        availableLanguages = availableLanguages,
        currentLanguage = currentLanguage,
        onLanguageSelected = { language ->
            viewModel.setLanguage(language)
        },
        onDismiss = { showLanguageDialog = false }
    )
}

@Preview(showBackground = true)
@Composable
fun WeatherAppPreview() {
    WeatherVoiceAppTheme {
        WeatherApp()
    }
}