package com.example.incidenciasapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.incidenciasapp.ui.screens.MapScreen
import com.example.incidenciasapp.ui.screens.ReportScreen
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidenciasApp(sendMessageToMQTT: (String, String, String) -> Unit) {
    var currentScreen by remember { mutableStateOf("map") } // Estado para manejar la pantalla actual

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Incidencias App") })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = "Mapa") },
                    label = { Text("Mapa") },
                    selected = currentScreen == "map",
                    onClick = { currentScreen = "map" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Warning, contentDescription = "Reporte") },
                    label = { Text("Reporte") },
                    selected = currentScreen == "report",
                    onClick = { currentScreen = "report" }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                "map" -> MapScreen()
                "report" -> ReportScreen(sendMessageToMQTT)
            }
        }
    }
}