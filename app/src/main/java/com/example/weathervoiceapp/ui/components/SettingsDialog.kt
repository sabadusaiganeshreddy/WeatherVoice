package com.example.weathervoiceapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun SettingsDialog(
    showDialog: Boolean,
    availableLanguages: List<Locale>,
    currentLanguage: Locale?,
    onLanguageSelected: (Locale) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Voice Settings") },
            text = {
                Column {
                    Text(
                        "Select Voice Language:",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    availableLanguages.forEach { locale ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = currentLanguage == locale,
                                    onClick = { onLanguageSelected(locale) }
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentLanguage == locale,
                                onClick = { onLanguageSelected(locale) }
                            )
                            Text(
                                text = getLanguageDisplayName(locale),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Done")
                }
            }
        )
    }
}

private fun getLanguageDisplayName(locale: Locale): String {
    return when (locale.language) {
        "en" -> "English"
        "hi" -> "हिंदी (Hindi)"
        "te" -> "తెలుగు (Telugu)"
        "ta" -> "தமிழ் (Tamil)"
        "bn" -> "বাংলা (Bengali)"
        "mr" -> "मराठी (Marathi)"
        "gu" -> "ગુજરાતી (Gujarati)"
        else -> locale.displayName
    }
}