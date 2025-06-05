package com.example.weathervoiceapp.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class TextToSpeechManager(private val context: Context) {

    private var textToSpeech: TextToSpeech? = null

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    private val _availableLanguages = MutableStateFlow<List<SupportedLanguage>>(emptyList())
    val availableLanguages: StateFlow<List<SupportedLanguage>> = _availableLanguages

    private val _currentLanguage = MutableStateFlow<SupportedLanguage?>(null)
    val currentLanguage: StateFlow<SupportedLanguage?> = _currentLanguage

    data class SupportedLanguage(
        val locale: Locale,
        val displayName: String,
        val nativeName: String,
        val isAvailable: Boolean = false
    )

    private val supportedLanguages = listOf(
        SupportedLanguage(Locale.ENGLISH, "English", "English"),
        SupportedLanguage(Locale("hi", "IN"), "Hindi", "हिंदी"),
        SupportedLanguage(Locale("te", "IN"), "Telugu", "తెలుగు"),
        SupportedLanguage(Locale("ta", "IN"), "Tamil", "தமிழ்"),
        SupportedLanguage(Locale("kn", "IN"), "Kannada", "ಕನ್ನಡ"),
        SupportedLanguage(Locale("bn", "IN"), "Bengali", "বাংলা"),
        SupportedLanguage(Locale("mr", "IN"), "Marathi", "मराठी"),
        SupportedLanguage(Locale("gu", "IN"), "Gujarati", "ગુજરાતી")
    )

    fun initialize() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.let { tts ->
                    checkLanguageAvailability(tts)
                    setupTTS(tts)
                    _isInitialized.value = true
                }
            }
        }
    }

    private fun checkLanguageAvailability(tts: TextToSpeech) {
        val availableLanguages = mutableListOf<SupportedLanguage>()

        supportedLanguages.forEach { language ->
            val result = tts.isLanguageAvailable(language.locale)
            val isAvailable = result == TextToSpeech.LANG_AVAILABLE ||
                    result == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                    result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE

            if (isAvailable) {
                availableLanguages.add(language.copy(isAvailable = true))
            }
        }

        // Always include English as fallback
        if (availableLanguages.none { it.locale.language == "en" }) {
            availableLanguages.add(supportedLanguages.first { it.locale.language == "en" }.copy(isAvailable = true))
        }

        _availableLanguages.value = availableLanguages

        // Set default language priority: Telugu > Hindi > Tamil > Kannada > English
        val defaultLanguage = availableLanguages.firstOrNull { it.locale.language == "te" }
            ?: availableLanguages.firstOrNull { it.locale.language == "hi" }
            ?: availableLanguages.firstOrNull { it.locale.language == "ta" }
            ?: availableLanguages.firstOrNull { it.locale.language == "kn" }
            ?: availableLanguages.first { it.locale.language == "en" }

        setLanguage(defaultLanguage)
    }

    private fun setupTTS(tts: TextToSpeech) {
        // Slower speech rate for better understanding
        tts.setSpeechRate(0.9f)
        tts.setPitch(1.0f)

        // Set up listener
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = true
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
            }

            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
            }
        })
    }

    fun setLanguage(language: SupportedLanguage) {
        textToSpeech?.let { tts ->
            val result = tts.setLanguage(language.locale)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                _currentLanguage.value = language
            }
        }
    }

    fun speak(text: String) {
        textToSpeech?.let { tts ->
            if (_isInitialized.value) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "weather_announcement")
            }
        }
    }

    fun stop() {
        textToSpeech?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        textToSpeech?.shutdown()
        textToSpeech = null
        _isInitialized.value = false
        _isSpeaking.value = false
    }
}