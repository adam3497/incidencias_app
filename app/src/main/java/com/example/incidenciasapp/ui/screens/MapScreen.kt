package com.example.incidenciasapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient

@Composable
fun MapScreen() {
    AndroidView(
        factory = {
            WebView(it).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl("http://10.0.2.2:1880/worldmap") // Cambia según tu configuración
            }
        },
        modifier = Modifier
            .fillMaxSize()
    )
}