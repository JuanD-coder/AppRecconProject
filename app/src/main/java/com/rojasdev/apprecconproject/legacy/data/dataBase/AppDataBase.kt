package com.rojasdev.apprecconproject.legacy.data.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rojasdev.apprecconproject.legacy.data.dao.RecolectoresDao
import com.rojasdev.apprecconproject.legacy.data.dao.RecollectionDao
import com.rojasdev.apprecconproject.legacy.data.dao.SettingDao
import com.rojasdev.apprecconproject.legacy.data.dao.WorkDao
import com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.legacy.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity
import com.rojasdev.apprecconproject.legacy.data.entities.WorkEntity

@Database(
    entities = [
        RecolectoresEntity::class,
        RecollectionEntity::class,
        SettingEntity::class,
        WorkEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun RecolectoresDao(): RecolectoresDao
    abstract fun RecollectionDao(): RecollectionDao
    abstract fun SettingDao(): SettingDao
    abstract fun WorkDao(): WorkDao

    companion object {
        private const val DATABASE_NAME: String = "DB_Reccon"

        @Volatile
        private var Instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                Instance = instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE WorkEntity (\n" +
                    "    PK_ID_Trabajo INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    cantidad INTEGER NOT NULL,\n" +
                    "    actividad TEXT NOT NULL,\n" +
                    "    Fecha TEXT NOT NULL,\n" +
                    "    Estado TEXT,\n" +
                    "    Fk_recolector INTEGER NOT NULL,\n" +
                    "    Fk_Configuracion INTEGER NOT NULL,\n" +
                    "    FOREIGN KEY (Fk_recolector) REFERENCES Recolectores(PK_ID_Recolector),\n" +
                    "    FOREIGN KEY (Fk_Configuracion) REFERENCES Configuracion(PK_ID_Configuracion)\n" +
                    ");"
        )
    }
}
