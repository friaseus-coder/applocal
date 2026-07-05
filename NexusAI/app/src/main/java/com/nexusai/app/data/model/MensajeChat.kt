package com.nexusai.app.data.model

data class MensajeChat(
    val id: Long = 0,
    val perfilId: Long,
    val remitente: String,
    val contenido: String,
    val timestamp: Long = System.currentTimeMillis()
)
