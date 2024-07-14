package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.dataModel.workSettings
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.data.entities.WorkEntity

@Dao
interface WorkDao {

    @Insert
    suspend fun insert(config: WorkEntity)

    @Query("SELECT Fk_recolector FROM WorkEntity WHERE Estado == 'active' ")
    suspend fun getFkIdCollectors(): List<Long>

    @Query("SELECT wor.PK_ID_Trabajo, wor.Fk_recolector, wor.actividad, " +
            "wor.Estado, wor.Fecha, wor.cantidad, wor.Fk_Configuracion, con.Precio as Precio FROM WorkEntity wor" +
            " INNER JOIN configuracion con ON wor.Fk_Configuracion = con.PK_ID_Configuracion" +
            " WHERE Fk_recolector == :men")
    suspend fun getWorkIdMen(men : Int): List<workSettings>

    @Query("SELECT sum(cantidad) FROM WorkEntity WHERE Estado == 'active' AND  Fk_recolector == :men")
    suspend fun getTotalDayWork(men: Int): Int

    @Query("SELECT sum(wor.cantidad * con.Precio ) FROM WorkEntity wor" +
            " INNER JOIN configuracion con ON wor.Fk_Configuracion = con.PK_ID_Configuracion" +
            " WHERE Fk_recolector == :men")
    suspend fun getTotalMoney(men : Int): Int

}