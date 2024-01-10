package com.example.websocket.model

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketModel(
    val type: String,
    val timestamp: Long,
    val direction: String,
    val speed: Double,
    val servo: Int,
    val action: String,
    val pose: String
)