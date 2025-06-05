# Weather Voice App 🌤️🗣️

A modern Android weather application built with **Jetpack Compose** and **Kotlin**, specifically designed for accessibility and rural users in India. The app features dual modes - a traditional visual interface and an innovative voice-first mode with multi-language Text-to-Speech support.

## 🌟 Features

### 🎯 Dual Mode Interface
- **Normal Mode**: Traditional weather app interface with clean Material 3 design
- **Voice Mode**: Large, accessible buttons with voice announcements for users with limited technical literacy

### 🗣️ Multi-Language Voice Support
- **Telugu** (తెలుగు)
- **Hindi** (हिंदी) 
- **Tamil** (தமிழ்)
- **Kannada** (ಕನ್ನಡ)
- **English**

### 🌾 Farming Intelligence
- Weather-based agricultural advice
- Seasonal farming recommendations
- Extreme weather alerts for crop protection
- Agricultural timing suggestions

### 📱 Core Weather Features
- Real-time current weather conditions
- 5-day detailed weather forecast
- Location-based weather data
- Weather icons and visual indicators
- Temperature ranges and precipitation probability

### ♿ Accessibility Features
- Text-to-Speech (TTS) in regional languages
- Large, touch-friendly buttons
- High contrast design
- Voice-first navigation
- Simplified UI for elderly and rural users

## 🛠️ Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Repository Pattern
- **Networking**: Retrofit + OkHttp
- **Location**: Google Play Services Location API
- **Text-to-Speech**: Android TTS API
- **API**: OpenWeatherMap API
- **Design**: Material 3 Design System

## 📋 Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 21+ (Android 5.0+)
- Kotlin 1.8+
- OpenWeatherMap API key

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/weather-voice-app.git
cd weather-voice-app
```

### 2. Get OpenWeatherMap API Key
1. Visit [OpenWeatherMap](https://openweathermap.org/api)
2. Sign up for a free account
3. Generate your API key

### 3. Configure API Key
Create or update `utils/Constants.kt`:
```kotlin
object Constants {
    const val WEATHER_API_KEY = "YOUR_API_KEY_HERE"
    const val DEFAULT_LATITUDE = 17.3850  // Your default location
    const val DEFAULT_LONGITUDE = 78.4867
}
```

### 4. Build and Run
1. Open project in Android Studio
2. Sync Gradle files
3. Connect Android device or start emulator
4. Click "Run" button

## 📦 Project Structure

```
app/
├── src/main/
│   ├── java/com/example/weathervoiceapp/
│   │   ├── data/
│   │   │   └── models/          # Data models
│   │   ├── network/             # API services
│   │   ├── repository/          # Data repository
│   │   ├── ui/
│   │   │   ├── components/      # Reusable UI components
│   │   │   └── theme/           # App theming
│   │   ├── utils/               # Utility classes
│   │   ├── viewmodel/           # ViewModels
│   │   └── MainActivity.kt      # Main activity
│   └── AndroidManifest.xml
├── build.gradle.kts
└── README.md
```

## 🔧 Configuration

### Permissions Required
The app requires the following permissions:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### Supported Languages
Configure TTS languages in `TextToSpeechManager.kt`. The app automatically detects available languages on the device.

## 🎨 Key Components

### 1. MainActivity
- Main entry point with dual-mode switching
- Location permission handling
- Navigation between normal and voice modes

### 2. WeatherViewModel
- Weather data management
- TTS initialization and control
- Location services integration

### 3. MultilingualWeatherSpeech
- Text-to-Speech content generation
- Multi-language weather descriptions
- Farming advice integration

### 4. LocationManager
- GPS location handling
- Permission management
- Location error handling

### 5. FarmingAdvisor
- Weather-based agricultural recommendations
- Seasonal farming tips
- Extreme weather alerts

## 🌍 Target Audience

### Primary Users
- **Rural farmers** in India seeking accessible weather information
- **Elderly users** who prefer voice interaction
- **Users with limited technical literacy**
- **Regional language speakers**

### Use Cases
- Daily weather planning for agricultural activities
- Voice-based weather updates for hands-free operation
- Multilingual weather information in native languages
- Farming decision support based on weather conditions

## 🔒 Security & Privacy

- API keys are not included in the repository
- Location data is processed locally
- No user data is stored permanently
- Weather data is fetched in real-time

## 🐛 Known Issues & Limitations

- Voice synthesis quality depends on device TTS engine
- Some regional languages may not be available on all devices
- OpenWeatherMap free tier has API call limitations (1000 calls/day)
- Location accuracy depends on device GPS capability

## 🚀 Future Enhancements

- [ ] Offline weather data caching
- [ ] Weather widgets for home screen
- [ ] Push notifications for weather alerts
- [ ] More regional languages support
- [ ] Integration with agricultural calendars
- [ ] Weather-based crop recommendation system

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Developer

**Your Name**
- GitHub: [@sabadusaiganeshreddy](https://github.com/sabadusaiganeshreddy)
- LinkedIn: [saiganeshreddysabadu](https://linkedin.com/in/saiganeshreddysabadu)
- Email: sgr92111@gmail.com

## 🙏 Acknowledgments

- [OpenWeatherMap](https://openweathermap.org/) for weather API
- [Material Design](https://material.io/) for design guidelines
- Android development community for inspiration
- Rural farmers who inspired this accessibility-focused approach

## 📱 Screenshots

### Normal Mode
- Current weather display with location
- 5-day forecast with weather icons
- Farming tips and recommendations

### Voice Mode
- Large accessibility buttons
- Language selection interface
- Voice status indicators

---

**Made with ❤️ for accessible weather information in India**

*This app aims to bridge the digital divide by making weather information accessible to users regardless of their technical literacy or preferred language.*
