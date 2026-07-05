package com.nexusai.app

import android.app.Application
import com.nexusai.app.data.local.AppDatabase
import com.nexusai.app.data.local.dao.GustoUsuarioDao
import com.nexusai.app.data.local.dao.MemoriaPerfilDao
import com.nexusai.app.data.local.dao.MensajeChatDao
import com.nexusai.app.data.local.dao.PerfilIADao
import com.nexusai.app.data.local.entity.PerfilIAEntity

class NexusAIApp : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var perfilDao: PerfilIADao
        private set

    lateinit var gustoDao: GustoUsuarioDao
        private set

    lateinit var memoriaDao: MemoriaPerfilDao
        private set

    lateinit var mensajeDao: MensajeChatDao
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = AppDatabase.getInstance(this)
        perfilDao = database.perfilIADao()
        gustoDao = database.gustoUsuarioDao()
        memoriaDao = database.memoriaPerfilDao()
        mensajeDao = database.mensajeChatDao()

        seedPerfiles()
    }

    private fun seedPerfiles() {
        val perfilesIniciales = listOf(
            PerfilIAEntity(
                nombre = "El Mejor Amigo",
                tipo = "AMIGO",
                descripcion = "Un oyente empático para apoyo emocional y charla casual diaria.",
                icono = "favorite",
                promptSistemaBase = "",
                tagsRag = "Psychology"
            ),
            PerfilIAEntity(
                nombre = "El Jefe / Mentor",
                tipo = "JEFE",
                descripcion = "Guía estratégica enfocada en el crecimiento profesional y la productividad.",
                icono = "school",
                promptSistemaBase = "",
                tagsRag = "Business"
            ),
            PerfilIAEntity(
                nombre = "El Analista de Inversiones",
                tipo = "ANALISTA",
                descripcion = "Asesor financiero estratégico con lógica de datos y gestión de riesgos.",
                icono = "analytics",
                promptSistemaBase = "",
                tagsRag = "Finance"
            ),
            PerfilIAEntity(
                nombre = "El Animador / Coach",
                tipo = "ANIMADOR",
                descripcion = "Coach motivacional de alta energía para romper la apatía.",
                icono = "whatshot",
                promptSistemaBase = "",
                tagsRag = "Motivation"
            ),
            PerfilIAEntity(
                nombre = "El Psicólogo",
                tipo = "PSICOLOGO",
                descripcion = "Terapeuta cognitivo-conductual para introspección guiada.",
                icono = "psychology",
                promptSistemaBase = "",
                tagsRag = "CBT"
            ),
            PerfilIAEntity(
                nombre = "La Pareja",
                tipo = "PAREJA",
                descripcion = "Acompañante afectivo con cariño, lealtad y ternura.",
                icono = "favorite",
                promptSistemaBase = "",
                tagsRag = "Relationship"
            ),
            PerfilIAEntity(
                nombre = "El Consejero Cristiano",
                tipo = "CRISTIANO",
                descripcion = "Consejero espiritual basado en las enseñanzas de Jesucristo.",
                icono = "church",
                promptSistemaBase = "",
                tagsRag = "Bible"
            ),
            PerfilIAEntity(
                nombre = "El Guía Musulmán",
                tipo = "MUSULMAN",
                descripcion = "Consejero espiritual inspirado en la sabiduría islámica clásica.",
                icono = "mosque",
                promptSistemaBase = "",
                tagsRag = "Quran"
            ),
            PerfilIAEntity(
                nombre = "El Maestro Budista",
                tipo = "BUDISTA",
                descripcion = "Maestro espiritual de compasión universal y desapego.",
                icono = "self_improvement",
                promptSistemaBase = "",
                tagsRag = "Dhammapada"
            ),
            PerfilIAEntity(
                nombre = "El Rabino Judío",
                tipo = "RABINO",
                descripcion = "Consejero espiritual basado en la Torá y la ética judía.",
                icono = "synagogue",
                promptSistemaBase = "",
                tagsRag = "Torah"
            ),
            PerfilIAEntity(
                nombre = "El Sabio Hinduista",
                tipo = "HINDUISTA",
                descripcion = "Guía espiritual del Dharma, Karma y paz interior del Atman.",
                icono = "temple_hindu",
                promptSistemaBase = "",
                tagsRag = "Bhagavad Gita"
            ),
            PerfilIAEntity(
                nombre = "El Estoico",
                tipo = "ESTOICO",
                descripcion = "Filósofo estoico para fortaleza mental y dicotomía del control.",
                icono = "shield",
                promptSistemaBase = "",
                tagsRag = "Philosophy"
            ),
            PerfilIAEntity(
                nombre = "El Preparador Físico",
                tipo = "ENTRENADOR",
                descripcion = "Entrenador personal para fitness, rutinas y prevención de lesiones.",
                icono = "fitness_center",
                promptSistemaBase = "",
                tagsRag = "Fitness"
            )
        )

        kotlinx.coroutines.runBlocking {
            perfilDao.insertAll(perfilesIniciales)
        }
    }

    companion object {
        lateinit var instance: NexusAIApp
            private set
    }
}
