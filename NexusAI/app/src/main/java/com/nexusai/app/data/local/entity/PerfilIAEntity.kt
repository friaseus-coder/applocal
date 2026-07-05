package com.nexusai.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfiles_ia")
data class PerfilIAEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val icono: String,
    val promptSistemaBase: String,
    val tagsRag: String,
    val isActive: Boolean = false
)
