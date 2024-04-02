package com.rojasdev.apprecconprojectPro.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "lote",
    foreignKeys = [
        ForeignKey(entity = FincaEntity::class, parentColumns = ["PK_ID_Finca"], childColumns = ["Fk_Finca"])
    ]
)
class LoteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Lote") var id: Int?,
    @ColumnInfo(name = "name_lote") var name: String,
    @ColumnInfo(name = "type_lote") var type: String,
    @ColumnInfo(name = "state") var state: Boolean,
    @ColumnInfo(name = "Fk_Finca") val finca:Int
)