package com.rojasdev.apprecconprojectPro.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Recoleccion",
            foreignKeys = [
                ForeignKey(entity = RecolectoresEntity::class, parentColumns = ["PK_ID_Recolector"], childColumns = ["Fk_recolector"]),
                ForeignKey(entity = SettingEntity::class, parentColumns = ["PK_ID_Configuracion"], childColumns = ["Fk_Configuracion"]),
                ForeignKey(entity = LoteEntity::class, parentColumns = ["PK_ID_Lote"], childColumns = ["Fk_Lote"])
            ]
    )
data class RecollectionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Recoleccion") val ID: Int?,
    @ColumnInfo(name = "Cantidad") val total: Double,
    @ColumnInfo(name = "Fecha") val date: String,
    @ColumnInfo(name = "Estado") val state: String?,
    @ColumnInfo(name = "Fk_recolector") val collector:Int,
    @ColumnInfo(name = "Fk_Configuracion") val setting:Int,
    @ColumnInfo(name = "Fk_Lote") val lote:Int,
    @ColumnInfo(name = "estado_firebase") var firebase: Boolean
)