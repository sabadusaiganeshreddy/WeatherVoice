package com.example.weathervoiceapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val locationPermissions = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            onPermissionGranted()
        }
    }

    when {
        locationPermissions.allPermissionsGranted -> {
            // Permissions granted, handled by LaunchedEffect
        }
        locationPermissions.shouldShowRationale -> {
            AlertDialog(
                onDismissRequest = { onPermissionDenied() },
                title = { Text("Location Permission Required") },
                text = {
                    Text("This app needs location access to provide accurate weather information for your area.")
                },
                confirmButton = {
                    TextButton(onClick = { locationPermissions.launchMultiplePermissionRequest() }) {
                        Text("Grant Permission")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onPermissionDenied() }) {
                        Text("Cancel")
                    }
                }
            )
        }
        else -> {
            // Request permissions
            LaunchedEffect(Unit) {
                locationPermissions.launchMultiplePermissionRequest()
            }
        }
    }
}