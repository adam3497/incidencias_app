package com.example.incidenciasapp.mqtt

import android.util.Log
import org.eclipse.paho.client.mqttv3.*


class MqttManager(brokerUrl: String, clientId: String) {
    private val mqttClient = MqttAsyncClient(brokerUrl, clientId, null)

    fun connect() {
        try {
            mqttClient.connect().waitForCompletion()
            mqttClient.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    Log.e("MQTT", "Conexión perdida: ${cause?.message}")
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    Log.d("MQTT", "Mensaje recibido en $topic: ${message?.toString()}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.d("MQTT", "Entrega completa")
                }
            })
            Log.d("MQTT", "Conexión exitosa al broker MQTT")
        } catch (e: MqttException) {
            Log.e("MQTT", "Error al conectar al broker MQTT: ${e.message}")
        }
    }

    fun publishMessage(topic: String, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            mqttClient.publish(topic, mqttMessage)
        } catch (e: MqttException) {
            Log.e("MQTT", "Error al publicar mensaje: ${e.message}")
        }
    }
}