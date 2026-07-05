package com.nexusai.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "historial_mensajes",
    foreignKeys = [
        ForeignKey(
            entity = PerfilIAEntity::class,
            parentColumns = ["id"],
            childColumns = ["perfilId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("perfilId")]
)
data class MensajeChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val perfilId: Long,
    val remitente: String,
    val contenido: String,
    val timestamp: Long = System.currentTimeMillis()
)
