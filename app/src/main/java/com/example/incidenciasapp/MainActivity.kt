package com.example.incidenciasapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.incidenciasapp.mqtt.MqttManager
import com.example.incidenciasapp.ui.IncidenciasApp
import org.eclipse.paho.client.mqttv3.MqttAsyncClient

class MainActivity : ComponentActivity() {

    private lateinit var mqttManager: MqttManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el MqttManager
        val brokerUrl = "tcp://10.0.2.2:1883" // Cambia según tu configuración
        val clientId = MqttAsyncClient.generateClientId()
        mqttManager = MqttManager(brokerUrl, clientId)

        // Conecta al broker MQTT
        mqttManager.connect()

        setContent {
            IncidenciasApp(::sendMessageToMQTT)
        }
    }

    private fun sendMessageToMQTT(type: String, description: String, city: String) {
        obtenerCoordenadas(city) { lat, lon ->
            if (lat != null && lon != null) {
                val topic = "incidencias/reporte" // Tema donde publicar el mensaje
                val message = """
                    {
                        "tipo": "$type",
                        "descripcion": "$description",
                        "lat": $lat,
                        "lon": $lon,
                        "usuario": "App Móvil"
                    }
                """.trimIndent() // Construye el mensaje JSON

                // Usa el MqttManager para publicar el mensaje
                mqttManager.publishMessage(topic, message)
            } else {
                runOnUiThread {
                    Toast.makeText(this, "No se pudieron obtener las coordenadas", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun obtenerCoordenadas(city: String, callback: (Double?, Double?) -> Unit) {
        val apiKey = "65b0c919e233434a90be008d3b35296c" // Cambia a tu clave de OpenCageData
        val url = "https://api.opencagedata.com/geocode/v1/json?q=$city&key=$apiKey"

        Thread {
            try {
                val client = okhttp3.OkHttpClient()
                val request = okhttp3.Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string()
                if (body != null) {
                    val json = org.json.JSONObject(body)
                    val results = json.getJSONArray("results")
                    if (results.length() > 0) {
                        val geometry = results.getJSONObject(0).getJSONObject("geometry")
                        val lat = geometry.getDouble("lat")
                        val lon = geometry.getDouble("lng")
                        callback(lat, lon)
                    } else {
                        callback(null, null)
                    }
                } else {
                    callback(null, null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null, null)
            }
        }.start()
    }

}