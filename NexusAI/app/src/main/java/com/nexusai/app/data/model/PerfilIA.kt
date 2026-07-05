package com.nexusai.app.data.model

data class PerfilIA(
    val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val icono: String,
    val promptSistemaBase: String,
    val tagsRag: String,
    val isActive: Boolean = false
)
