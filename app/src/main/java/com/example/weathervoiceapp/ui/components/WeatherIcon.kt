package com.example.weathervoiceapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun WeatherIcon(
    weatherMain: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    val icon = getWeatherIcon(weatherMain)
    Icon(
        imageVector = icon,
        contentDescription = weatherMain,
        modifier = modifier.size(48.dp),
        tint = tint
    )
}

private fun getWeatherIcon(weatherMain: String): ImageVector {
    return when (weatherMain.lowercase()) {
        "clear" -> Icons.Default.WbSunny
        "clouds" -> Icons.Default.Cloud
        "rain" -> Icons.Default.Umbrella
        "drizzle" -> Icons.Default.Grain
        "thunderstorm" -> Icons.Default.Bolt
        "snow" -> Icons.Default.AcUnit
        "mist", "fog", "haze", "smoke" -> Icons.Default.Visibility
        "dust", "sand", "ash" -> Icons.Default.Air
        "squall", "tornado" -> Icons.Default.Cyclone
        else -> Icons.Default.WbSunny
    }
}