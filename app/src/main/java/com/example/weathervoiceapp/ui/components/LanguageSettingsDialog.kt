package com.example.weathervoiceapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weathervoiceapp.utils.TextToSpeechManager

@Composable
fun LanguageSettingsDialog(
    showDialog: Boolean,
    availableLanguages: List<TextToSpeechManager.SupportedLanguage>,
    currentLanguage: TextToSpeechManager.SupportedLanguage?,
    onLanguageSelected: (TextToSpeechManager.SupportedLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(onDismissRequest = onDismiss,
            title = {
                Text(
                    "🗣️ Voice Language / भाषा चुनें",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Column {
                    Text(
                        "Select your preferred voice language:",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    availableLanguages.forEach { language ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = currentLanguage?.locale?.language == language.locale.language,
                                    onClick = { onLanguageSelected(language) }
                                )
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentLanguage?.locale?.language == language.locale.language,
                                onClick = { onLanguageSelected(language) }
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = language.nativeName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = language.displayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            if (language.isAvailable) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.CheckCircle,
                                    contentDescription = "Available",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Note: Language availability depends on your device's Text-to-Speech settings.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Done / पूर्ण")
                }
            }
        )
    }
}