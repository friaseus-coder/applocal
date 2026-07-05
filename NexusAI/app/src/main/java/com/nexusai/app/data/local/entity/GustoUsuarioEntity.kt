package com.nexusai.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gustos_usuario")
data class GustoUsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoria: String,
    val elemento: String,
    val sentimiento: String,
    val fechaDescubrimiento: Long = System.currentTimeMillis()
)
