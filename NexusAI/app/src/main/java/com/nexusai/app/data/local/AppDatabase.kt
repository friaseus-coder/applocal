package com.nexusai.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nexusai.app.data.local.converter.Converters
import com.nexusai.app.data.local.dao.GustoUsuarioDao
import com.nexusai.app.data.local.dao.MemoriaPerfilDao
import com.nexusai.app.data.local.dao.MensajeChatDao
import com.nexusai.app.data.local.dao.PerfilIADao
import com.nexusai.app.data.local.entity.GustoUsuarioEntity
import com.nexusai.app.data.local.entity.MemoriaPerfilEntity
import com.nexusai.app.data.local.entity.MensajeChatEntity
import com.nexusai.app.data.local.entity.PerfilIAEntity

@Database(
    entities = [
        PerfilIAEntity::class,
        GustoUsuarioEntity::class,
        MemoriaPerfilEntity::class,
        MensajeChatEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun perfilIADao(): PerfilIADao
    abstract fun gustoUsuarioDao(): GustoUsuarioDao
    abstract fun memoriaPerfilDao(): MemoriaPerfilDao
    abstract fun mensajeChatDao(): MensajeChatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nexus_ai_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
