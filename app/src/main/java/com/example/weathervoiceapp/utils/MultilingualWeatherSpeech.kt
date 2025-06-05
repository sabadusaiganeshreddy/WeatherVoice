package com.example.weathervoiceapp.utils

import com.example.weathervoiceapp.data.models.WeatherData
import java.text.SimpleDateFormat
import java.util.*

object MultilingualWeatherSpeech {

    fun generateCurrentWeatherSpeech(weatherData: WeatherData, language: String): String {
        return when (language) {
            "hi" -> generateHindiCurrentWeather(weatherData)
            "te" -> generateTeluguCurrentWeather(weatherData)
            "ta" -> generateTamilCurrentWeather(weatherData)
            "kn" -> generateKannadaCurrentWeather(weatherData)
            else -> generateEnglishCurrentWeather(weatherData)
        }
    }

    fun generateForecastSpeech(weatherData: WeatherData, language: String): String {
        return when (language) {
            "hi" -> generateHindiForecast(weatherData)
            "te" -> generateTeluguForecast(weatherData)
            "ta" -> generateTamilForecast(weatherData)
            "kn" -> generateKannadaForecast(weatherData)
            else -> generateEnglishForecast(weatherData)
        }
    }

    // ENGLISH METHODS (Original)
    private fun generateEnglishCurrentWeather(weatherData: WeatherData): String {
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

    private fun generateEnglishForecast(weatherData: WeatherData): String {
        val forecast = weatherData.forecast
        val dailyForecasts = forecast.list.chunked(8).take(5)

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

                // Add farming advice for each day
                when {
                    rainChance > 70 -> append("Good day for indoor farm work. ")
                    temp > 30 && rainChance < 20 -> append("Good day for watering crops. ")
                    temp < 15 -> append("Protect sensitive plants from cold. ")
                }
            }

            append("That's your 5-day forecast. Plan your activities accordingly.")
        }
    }

    // HINDI METHODS (Original)
    private fun generateHindiCurrentWeather(weatherData: WeatherData): String {
        val current = weatherData.current
        val location = current.name
        val temperature = current.main.temp.toInt()
        val description = getHindiWeatherDescription(current.weather.firstOrNull()?.main ?: "Clear")
        val feelsLike = current.main.feels_like.toInt()
        val humidity = current.main.humidity
        val windSpeed = current.wind.speed.toInt()

        return buildString {
            append("नमस्कार! यहाँ है $location का मौसम। ")
            append("तापमान $temperature डिग्री सेल्सियस है। ")
            append("मौसम $description है। ")
            append("$feelsLike डिग्री जैसा महसूस हो रहा है। ")
            append("नमी $humidity प्रतिशत है। ")
            append("हवा की गति $windSpeed मीटर प्रति सेकंड है। ")

            when {
                temperature > 35 -> append("आज बहुत गर्मी है। पानी पिएं और धूप से बचें। ")
                temperature < 10 -> append("आज ठंड है। गर्म कपड़े पहनें। ")
                humidity > 80 -> append("आज बहुत उमस है। ")
                windSpeed > 10 -> append("आज हवा तेज है। ")
            }

            if (current.weather.any { it.main.contains("rain", true) }) {
                append("छाता ले जाना न भूलें क्योंकि बारिश हो सकती है। ")
            }
        }
    }

    private fun generateHindiForecast(weatherData: WeatherData): String {
        val forecast = weatherData.forecast
        val dailyForecasts = forecast.list.chunked(8).take(5)

        return buildString {
            append("यहाँ है 5 दिन का मौसम पूर्वानुमान। ")

            dailyForecasts.forEachIndexed { index, dayForecasts ->
                val dayForecast = dayForecasts.first()
                val temp = dayForecast.main.temp.toInt()
                val description = getHindiWeatherDescription(dayForecast.weather.first().main)
                val rainChance = (dayForecast.pop * 100).toInt()

                when (index) {
                    0 -> append("आज, ")
                    1 -> append("कल, ")
                    2 -> append("परसों, ")
                    else -> append("${index + 1} दिन बाद, ")
                }

                append("तापमान $temp डिग्री होगा और मौसम $description होगा। ")

                if (rainChance > 30) {
                    append("बारिश की $rainChance प्रतिशत संभावना है। ")
                }
            }

            append("यह था आपका 5 दिन का मौसम पूर्वानुमान। अपनी गतिविधियों की योजना बनाएं।")
        }
    }

    // TELUGU METHODS (IMPROVED VERSION)
    private fun generateTeluguCurrentWeather(weatherData: WeatherData): String {
        val current = weatherData.current
        val location = current.name
        val temperature = current.main.temp.toInt()
        val description = getTeluguWeatherDescription(current.weather.firstOrNull()?.main ?: "Clear")
        val feelsLike = current.main.feels_like.toInt()
        val humidity = current.main.humidity
        val windSpeed = current.wind.speed.toInt()

        return buildString {
            append("నమస్కారం! ఇది $location వాతావరణ వివరాలు. ")
            append("ఈరోజు ఉష్ణోగ్రత $temperature డిగ్రీ సెల్సియస్ ఉంది. ")
            append("వాతావరణం $description ఉంది. ")
            append("$feelsLike డిగ్రీల వలె అనిపిస్తోంది. ")
            append("గాలిలో తేమ $humidity శాతం ఉంది. ")
            append("గాలి వేగం సెకనుకు $windSpeed మీటర్లు ఉంది. ")

            // Farming advice in Telugu
            val farmingAdvice = getTeluguFarmingAdvice(weatherData)
            if (farmingAdvice.isNotEmpty()) {
                append("వ్యవసాయ సలహాలు: ")
                farmingAdvice.forEach { advice ->
                    append("$advice ")
                }
            }

            when {
                temperature > 35 -> append("ఈరోజు చాలా వేడిమిగా ఉంది. నీళ్లు ఎక్కువగా తాగండి మరియు ఎండ వేళల్లో బయటకు వెళ్లవద్దు. ")
                temperature < 10 -> append("ఈరోజు చలిగా ఉంది. వెచ్చని బట్టలు ధరించండి. ")
                humidity > 80 -> append("ఈరోజు చాలా తేమగా ఉంది. ")
                windSpeed > 10 -> append("ఈరోజు గాలులు వేగంగా ఉన్నాయి. ")
            }

            if (current.weather.any { it.main.contains("rain", true) }) {
                append("వర్షం వచ్చే అవకాశం ఉంది కాబట్టి గొడుగు తీసుకెళ్లండి. ")
            }
        }
    }

    private fun generateTeluguForecast(weatherData: WeatherData): String {
        val forecast = weatherData.forecast
        val dailyForecasts = forecast.list.chunked(8).take(5)

        return buildString {
            append("ఐదు రోజుల వాతావరణ సమాచారం మరియు వ్యవసాయ మార్గదర్శకం వినండి. ")

            dailyForecasts.forEachIndexed { index, dayForecasts ->
                val dayForecast = dayForecasts.first()
                val temp = dayForecast.main.temp.toInt()
                val description = getTeluguWeatherDescription(dayForecast.weather.first().main)
                val rainChance = (dayForecast.pop * 100).toInt()

                when (index) {
                    0 -> append("ఈరోజు ")
                    1 -> append("రేపు ")
                    2 -> append("ఎల్లుండి ")
                    3 -> append("నాలుగవ రోజు ")
                    4 -> append("ఐదవ రోజు ")
                }

                append("ఉష్ణోగ్రత $temp డిగ్రీలు ఉంటుంది. వాతావరణం $description  ఉంటుంది. ")

                if (rainChance > 30) {
                    append("వర్షం  రావడానికి $rainChance శాతం అవకాశం ఉంది. ")
                }

                // Add farming advice for each day
                when {
                    rainChance > 70 -> append("వర్షం ఎక్కువగా ఉంటుంది కాబట్టి ఇంట్లో పని చేయండి. ")
                    temp > 30 && rainChance < 20 -> append("వేడి ఎక్కువ ఉంది కాబట్టి మొక్కలకు నీళ్లు పోయండి. ")
                    temp < 15 -> append("చలిగా ఉంటుంది కాబట్టి మొక్కలను కాపాడండి. ")
                }
            }

            append("ఇది మీ ఐదు రోజుల వాతావరణ సమాచారం. దీని ఆధారంగా మీ వ్యవసాయ పనులను ప్లాన్ చేసుకోండి.")
        }
    }

    private fun getTeluguFarmingAdvice(weatherData: WeatherData): List<String> {
        val advice = mutableListOf<String>()
        val current = weatherData.current
        val forecast = weatherData.forecast

        when {
            current.main.temp > 35 -> {
                advice.add("చాలా వేడిగా ఉంది - తెల్లవారుజామున లేదా సాయంత్రం పంటలకు నీళ్లు పోయండి")
                advice.add("పశువులను వేడి నుండి కాపాడండి")
            }
            current.main.temp < 10 -> {
                advice.add("చలిగా ఉంది - చిన్న మొక్కలను కాపాడండి")
                advice.add("మంచు నుండి పంటలను కాపాడండి")
            }
        }

        val rainToday = forecast.list.take(8).any { it.pop > 0.7 }
        when {
            rainToday -> {
                advice.add("వర్షం వస్తుంది - మందుల  కొట్టడం వాయిదా వేయండి")
                advice.add("ఇంట్లో చేయగల పనులు చేయండి")
            }
            current.main.humidity < 40 -> {
                advice.add("తేమ తక్కువగా ఉంది - నీళ్లు ఎక్కువగా పోయండి")
            }
        }

        if (current.wind.speed > 10) {
            advice.add("గాలి వేగంగా ఉంది - మందుల  కొట్టడం చేయవద్దు")
        }

        return advice
    }

    // TAMIL METHODS (Original)
    private fun generateTamilCurrentWeather(weatherData: WeatherData): String {
        val current = weatherData.current
        val location = current.name
        val temperature = current.main.temp.toInt()
        val description = getTamilWeatherDescription(current.weather.firstOrNull()?.main ?: "Clear")
        val feelsLike = current.main.feels_like.toInt()
        val humidity = current.main.humidity
        val windSpeed = current.wind.speed.toInt()

        return buildString {
            append("வணக்கம்! இதோ $location இன் வானிலை. ")
            append("வெப்பநிலை $temperature டிகிரி செல்சியஸ். ")
            append("வானிலை $description ஆக உள்ளது. ")
            append("$feelsLike டிகிரி போல் உணர்கிறது. ")
            append("ஈரப்பதம் $humidity சதவீதம். ")
            append("காற்றின் வேகம் வினாடிக்கு $windSpeed மீட்டர். ")

            when {
                temperature > 35 -> append("இன்று மிகவும் வெப்பமாக உள்ளது. தண்ணீர் குடியுங்கள் மற்றும் வெயிலில் இருந்து தவிர்த்துக் கொள்ளுங்கள். ")
                temperature < 10 -> append("இன்று குளிராக உள்ளது. வெதுவெதுப்பான உடைகளை அணியுங்கள். ")
                humidity > 80 -> append("இன்று மிகவும் ஈரப்பதமாக உள்ளது। ")
                windSpeed > 10 -> append("இன்று காற்று வேகமாக உள்ளது। ")
            }

            if (current.weather.any { it.main.contains("rain", true) }) {
                append("மழை பெய்ய வாய்ப்பு உள்ளதால் குடை எடுத்துச் செல்ல மறக்காதீர்கள். ")
            }
        }
    }

    private fun generateTamilForecast(weatherData: WeatherData): String {
        val forecast = weatherData.forecast
        val dailyForecasts = forecast.list.chunked(8).take(5)

        return buildString {
            append("இதோ 5 நாள் வானிலை முன்னறிவிப்பு. ")

            dailyForecasts.forEachIndexed { index, dayForecasts ->
                val dayForecast = dayForecasts.first()
                val temp = dayForecast.main.temp.toInt()
                val description = getTamilWeatherDescription(dayForecast.weather.first().main)
                val rainChance = (dayForecast.pop * 100).toInt()

                when (index) {
                    0 -> append("இன்று, ")
                    1 -> append("நாளை, ")
                    2 -> append("நாளை மறுநாள், ")
                    else -> append("${index + 1} நாட்களுக்குப் பிறகு, ")
                }

                append("வெப்பநிலை $temp டிகிரி இருக்கும் மற்றும் வானிலை $description ஆக இருக்கும். ")

                if (rainChance > 30) {
                    append("மழைக்கான வாய்ப்பு $rainChance சதவீதம் உள்ளது. ")
                }
            }

            append("இது உங்கள் 5 நாள் வானிலை முன்னறிவிப்பு. அதற்கேற்ப உங்கள் செயல்பாடுகளைத் திட்டமிடுங்கள்.")
        }
    }

    // KANNADA METHODS (Original)
    private fun generateKannadaCurrentWeather(weatherData: WeatherData): String {
        val current = weatherData.current
        val location = current.name
        val temperature = current.main.temp.toInt()
        val description = getKannadaWeatherDescription(current.weather.firstOrNull()?.main ?: "Clear")
        val feelsLike = current.main.feels_like.toInt()
        val humidity = current.main.humidity
        val windSpeed = current.wind.speed.toInt()

        return buildString {
            append("ನಮಸ್ಕಾರ! ಇಲ್ಲಿ $location ಹವಾಮಾನ. ")
            append("ತಾಪಮಾನ $temperature ಡಿಗ್ರಿ ಸೆಲ್ಸಿಯಸ್. ")
            append("ಹವಾಮಾನ $description ಇದೆ. ")
            append("$feelsLike ಡಿಗ್ರಿಯಂತೆ ಅನಿಸುತ್ತಿದೆ. ")
            append("ತೇವಾಂಶ $humidity ಶೇಕಡಾ. ")
            append("ಗಾಳಿಯ ವೇಗ ಸೆಕೆಂಡಿಗೆ $windSpeed ಮೀಟರ್. ")

            when {
                temperature > 35 -> append("ಇಂದು ತುಂಬಾ ಬಿಸಿಯಾಗಿದೆ. ನೀರು ಕುಡಿಯಿರಿ ಮತ್ತು ಬಿಸಿಲಿನಿಂದ ದೂರವಿರಿ. ")
                temperature < 10 -> append("ಇಂದು ತಣ್ಣಗಿದೆ. ಬೆಚ್ಚಗಿನ ಬಟ್ಟೆಗಳನ್ನು ಧರಿಸಿ. ")
                humidity > 80 -> append("ಇಂದು ತುಂಬಾ ತೇವಾಂಶವಿದೆ. ")
                windSpeed > 10 -> append("ಇಂದು ಗಾಳಿ ವೇಗವಾಗಿದೆ. ")
            }

            if (current.weather.any { it.main.contains("rain", true) }) {
                append("ಮಳೆ ಬರುವ ಸಾಧ್ಯತೆ ಇರುವುದರಿಂದ ಛತ್ರಿ ತೆಗೆದುಕೊಂಡು ಹೋಗಲು ಮರೆಯಬೇಡಿ. ")
            }
        }
    }

    private fun generateKannadaForecast(weatherData: WeatherData): String {
        val forecast = weatherData.forecast
        val dailyForecasts = forecast.list.chunked(8).take(5)

        return buildString {
            append("ಇಲ್ಲಿ 5 ದಿನಗಳ ಹವಾಮಾನ ಮುನ್ಸೂಚನೆ. ")

            dailyForecasts.forEachIndexed { index, dayForecasts ->
                val dayForecast = dayForecasts.first()
                val temp = dayForecast.main.temp.toInt()
                val description = getKannadaWeatherDescription(dayForecast.weather.first().main)
                val rainChance = (dayForecast.pop * 100).toInt()

                when (index) {
                    0 -> append("ಇಂದು, ")
                    1 -> append("ನಾಳೆ, ")
                    2 -> append("ನಾಡಿದ್ದು, ")
                    else -> append("${index + 1} ದಿನಗಳ ನಂತರ, ")
                }

                append("ತಾಪಮಾನ $temp ಡಿಗ್ರಿ ಇರುತ್ತದೆ ಮತ್ತು ಹವಾಮಾನ $description ಇರುತ್ತದೆ. ")

                if (rainChance > 30) {
                    append("ಮಳೆಯ ಸಾಧ್ಯತೆ $rainChance ಶೇಕಡಾ ಇದೆ. ")
                }
            }

            append("ಇದು ನಿಮ್ಮ 5 ದಿನಗಳ ಹವಾಮಾನ ಮುನ್ಸೂಚನೆ. ಅದರ ಪ್ರಕಾರ ನಿಮ್ಮ ಚಟುವಟಿಕೆಗಳನ್ನು ಯೋಜಿಸಿ.")
        }
    }

    // WEATHER DESCRIPTION TRANSLATORS
    private fun getHindiWeatherDescription(weatherMain: String): String {
        return when (weatherMain.lowercase()) {
            "clear" -> "साफ"
            "clouds" -> "बादल"
            "rain" -> "बारिश"
            "drizzle" -> "फुहार"
            "thunderstorm" -> "तूफान"
            "snow" -> "बर्फ"
            "mist", "fog" -> "कोहरा"
            else -> "सामान्य"
        }
    }

    private fun getTeluguWeatherDescription(weatherMain: String): String {
        return when (weatherMain.lowercase()) {
            "clear" -> "ఆకాశం స్పష్టంగా"
            "clouds" -> "మేఘాలతో"
            "rain" -> "వర్షంతో"
            "drizzle" -> "చినుకులతో"
            "thunderstorm" -> "ఉరుములు మరియు వర్షంతో"
            "snow" -> "మంచుతో"
            "mist", "fog" -> "పొగమంచుతో"
            "dust" -> "దుమ్ముతో"
            "haze" -> "మబ్బుతో"
            else -> "సాధారణంగా"
        }
    }

    private fun getTamilWeatherDescription(weatherMain: String): String {
        return when (weatherMain.lowercase()) {
            "clear" -> "தெளிவான"
            "clouds" -> "மேகங்கள்"
            "rain" -> "மழை"
            "drizzle" -> "தூறல்"
            "thunderstorm" -> "இடியுடன் கூடிய மழை"
            "snow" -> "பனி"
            "mist", "fog" -> "மூடுபனி"
            else -> "சாதாரண"
        }
    }

    private fun getKannadaWeatherDescription(weatherMain: String): String {
        return when (weatherMain.lowercase()) {
            "clear" -> "ಸ್ಪಷ್ಟ"
            "clouds" -> "ಮೋಡಗಳು"
            "rain" -> "ಮಳೆ"
            "drizzle" -> "ಚಿಮುಕಿಸುವ ಮಳೆ"
            "thunderstorm" -> "ಗುಡುಗುಸಹಿತ ಮಳೆ"
            "snow" -> "ಹಿಮ"
            "mist", "fog" -> "ಮಂಜು"
            else -> "ಸಾಮಾನ್ಯ"
        }
    }
}