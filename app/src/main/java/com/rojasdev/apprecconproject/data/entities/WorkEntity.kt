package com.rojasdev.apprecconproject.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "WorkEntity",
    foreignKeys = [
        ForeignKey(
            entity = RecolectoresEntity::class,
            parentColumns = ["PK_ID_Recolector"],
            childColumns = ["Fk_recolector"]
        ),
        ForeignKey(
            entity = SettingEntity::class,
            parentColumns = ["PK_ID_Configuracion"],
            childColumns = ["Fk_Configuracion"]
        )
    ]
)

class WorkEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Trabajo") val ID: Int?,
    @ColumnInfo(name = "cantidad") val amount: Int,
    @ColumnInfo(name = "actividad") val total: String,
    @ColumnInfo(name = "Fecha") val date: String,
    @ColumnInfo(name = "Estado") val state: String?,
    @ColumnInfo(name = "Fk_recolector") val collector: Int,
    @ColumnInfo(name = "Fk_Configuracion") val setting: Int
)