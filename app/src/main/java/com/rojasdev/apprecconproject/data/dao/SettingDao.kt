package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.dataModel.totalPdf
import com.rojasdev.apprecconproject.data.dataModel.totalWeekPdf
import com.rojasdev.apprecconproject.data.entities.SettingEntity

@Dao
interface SettingDao {

    @Insert
    suspend fun insertConfig(config: SettingEntity)

    @Query("UPDATE configuracion SET Estado = :status WHERE PK_ID_Configuracion == :id")
    suspend fun updateConfig(id: Int?, status: String)

    @Query("SELECT * FROM configuracion WHERE Estado == 'active'")
    suspend fun getAliment(): List<SettingEntity>

    @Query("SELECT * FROM configuracion WHERE Estado == 'archived' ORDER BY PK_ID_Configuracion DESC")
    suspend fun getAlimentArchived(): List<SettingEntity>

    @Query("SELECT * FROM configuracion WHERE Estado == :state ")
    suspend fun getAlimentState(state: String): List<SettingEntity>

    @Query(
        "SELECT con.Precio, sum(re.Cantidad) as result, sum(re.Cantidad * con.Precio) as total " +
                "FROM configuracion con  " +
                "INNER JOIN Recoleccion re ON con.PK_ID_Configuracion = re.Fk_Configuracion " +
                "WHERE re.Fecha >= :startDate AND re.Fecha <= :endDate "
    )
    suspend fun getTotalPdfWeek(startDate: String, endDate: String): List<totalPdf>

    @Query(
        "SELECT con.Precio, sum(re.Cantidad) as cantidad, sum(re.Cantidad * con.Precio) as total " +
                "FROM configuracion con  " +
                "INNER JOIN Recoleccion re ON con.PK_ID_Configuracion = re.Fk_Configuracion " +
                "WHERE re.Estado == 'active'"
    )
    suspend fun getTotalCollectionActive(): List<totalWeekPdf>

    @Query("Delete FROM Configuracion")
    suspend fun delete()
}