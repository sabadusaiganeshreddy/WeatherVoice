package com.example.weathervoiceapp.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathervoiceapp.data.models.WeatherData
import com.example.weathervoiceapp.repository.WeatherRepository
import com.example.weathervoiceapp.utils.Constants
import com.example.weathervoiceapp.utils.LocationManager
import com.example.weathervoiceapp.utils.TextToSpeechManager
import com.example.weathervoiceapp.utils.MultilingualWeatherSpeech
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _weatherData = mutableStateOf<WeatherData?>(null)
    val weatherData: State<WeatherData?> = _weatherData

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _locationEnabled = mutableStateOf(false)
    val locationEnabled: State<Boolean> = _locationEnabled

    private val _isTtsReady = mutableStateOf(false)
    val isTtsReady: State<Boolean> = _isTtsReady

    private val _isSpeaking = mutableStateOf(false)
    val isSpeaking: State<Boolean> = _isSpeaking

    private val _availableLanguages = mutableStateOf<List<TextToSpeechManager.SupportedLanguage>>(emptyList())
    val availableLanguages: State<List<TextToSpeechManager.SupportedLanguage>> = _availableLanguages

    private val _currentLanguage = mutableStateOf<TextToSpeechManager.SupportedLanguage?>(null)
    val currentLanguage: State<TextToSpeechManager.SupportedLanguage?> = _currentLanguage

    private var locationManager: LocationManager? = null
    private var ttsManager: TextToSpeechManager? = null

    init {
        // Load default weather data
        loadWeatherData(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE)
    }

    fun initializeServices(context: Context) {
        // Initialize location manager
        locationManager = LocationManager(context)

        // Initialize TTS
        ttsManager = TextToSpeechManager(context)
        ttsManager?.initialize()

        // Observe TTS state
        viewModelScope.launch {
            ttsManager?.isInitialized?.collectLatest { isReady ->
                _isTtsReady.value = isReady
            }
        }

        viewModelScope.launch {
            ttsManager?.isSpeaking?.collectLatest { speaking ->
                _isSpeaking.value = speaking
            }
        }

        viewModelScope.launch {
            ttsManager?.availableLanguages?.collectLatest { languages ->
                _availableLanguages.value = languages
            }
        }

        viewModelScope.launch {
            ttsManager?.currentLanguage?.collectLatest { language ->
                _currentLanguage.value = language
            }
        }
    }

    fun setLanguage(language: TextToSpeechManager.SupportedLanguage) {
        ttsManager?.setLanguage(language)
    }

    fun speakCurrentWeather() {
        _weatherData.value?.let { weather ->
            val languageCode = _currentLanguage.value?.locale?.language ?: "en"
            val speechText = MultilingualWeatherSpeech.generateCurrentWeatherSpeech(weather, languageCode)
            ttsManager?.speak(speechText)
        } ?: run {
            val languageCode = _currentLanguage.value?.locale?.language ?: "en"
            val errorMessage = when (languageCode) {
                "hi" -> "मौसम की जानकारी उपलब्ध नहीं है। कृपया रीफ्रेश करें और फिर से कोशिश करें।"
                "te" -> "వాతావరణ సమాచారం అందుబాటులో లేదు. దయచేసి రీఫ్రెష్ చేసి మళ్లీ ప్రయత్నించండి."
                "ta" -> "வானிலை தகவல் கிடைக்கவில்லை. தயவுசெய்து புதுப்பித்து மீண்டும் முயற்சிக்கவும்."
                "kn" -> "ಹವಾಮಾನ ಮಾಹಿತಿ ಲಭ್ಯವಿಲ್ಲ. ದಯವಿಟ್ಟು ರಿಫ್ರೆಶ್ ಮಾಡಿ ಮತ್ತು ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ."
                else -> "Weather data is not available. Please refresh and try again."
            }
            ttsManager?.speak(errorMessage)
        }
    }

    fun speakForecast() {
        _weatherData.value?.let { weather ->
            val languageCode = _currentLanguage.value?.locale?.language ?: "en"
            val speechText = MultilingualWeatherSpeech.generateForecastSpeech(weather, languageCode)
            ttsManager?.speak(speechText)
        } ?: run {
            val languageCode = _currentLanguage.value?.locale?.language ?: "en"
            val errorMessage = when (languageCode) {
                "hi" -> "मौसम पूर्वानुमान उपलब्ध नहीं है। कृपया रीफ्रेश करें और फिर से कोशिश करें।"
                "te" -> "వాతావరణ సమాచారం అందుబాటులో లేదు. దయచేసి రీఫ్రెష్ చేసి మళ్లీ ప్రయత్నించండి."
                "ta" -> "வானிலை முன்னறிவிப்பு கிடைக்கவில்லை. தயவுசெய்து புதুப்பித்து மீண்டும் முயற்சிக்கவும்."
                "kn" -> "ಹವಾಮಾನ ಮುನ್ಸೂಚನೆ ಲಭ್ಯವಿಲ್ಲ. ದಯವಿಟ್ಟು ರಿಫ್ರೆಶ್ ಮಾಡಿ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ."
                else -> "Weather forecast is not available. Please refresh and try again."
            }
            ttsManager?.speak(errorMessage)
        }
    }

    fun stopSpeaking() {
        ttsManager?.stop()
    }

    fun loadCurrentLocationWeather() {
        locationManager?.let { manager ->
            viewModelScope.launch {
                _isLoading.value = true
                _error.value = null

                manager.getCurrentLocation()
                    .onSuccess { location ->
                        _locationEnabled.value = true
                        loadWeatherData(location.latitude, location.longitude)
                    }
                    .onFailure { exception ->
                        _locationEnabled.value = false
                        _error.value = "Location error: ${exception.message}"
                        _isLoading.value = false
                    }
            }
        } ?: run {
            _error.value = "Location manager not initialized"
        }
    }

    fun loadWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getWeatherData(latitude, longitude)
                .onSuccess { weatherData ->
                    _weatherData.value = weatherData
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Unknown error occurred"
                }

            _isLoading.value = false
        }
    }

    fun refreshWeather() {
        if (_locationEnabled.value) {
            loadCurrentLocationWeather()
        } else {
            _weatherData.value?.let {
                loadWeatherData(it.current.coord.lat, it.current.coord.lon)
            } ?: loadWeatherData(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager?.shutdown()
    }
}