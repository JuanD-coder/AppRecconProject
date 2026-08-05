package com.rojasdev.apprecconproject.legacy.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.legacy.data.dataModel.totalPdf
import com.rojasdev.apprecconproject.legacy.data.dataModel.totalWeekPdf
import com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity

@Dao
interface SettingDao {

    @Insert
    suspend fun insertConfig(config: SettingEntity)

    @Query("UPDATE configuracion SET Estado = :status WHERE PK_ID_Configuracion == :id")
    suspend fun updateConfig(id: Int?, status: String)

    @Query("SELECT * FROM configuracion WHERE Estado == 'active' AND Alimentacion == :aliment")
    suspend fun getAliment(aliment: String): List<SettingEntity>

    @Query("SELECT * FROM configuracion WHERE Estado == 'active' AND Alimentacion != 'yes' " +
           "AND Alimentacion != 'no'")
    suspend fun getPriceWork(): List<SettingEntity>

    @Query("SELECT COUNT(*) FROM configuracion WHERE Estado == 'active' AND Alimentacion != 'yes' AND Alimentacion != 'no'")
    suspend fun getPriceWorkCount(): Int

    @Query("SELECT * FROM configuracion WHERE Estado == 'archived' ORDER BY PK_ID_Configuracion DESC")
    suspend fun getAlimentArchived(): List<SettingEntity>

    @Query("SELECT * FROM configuracion WHERE Estado == :state ")
    suspend fun getAlimentState(state: String): List<SettingEntity>

    @Query("SELECT * FROM configuracion WHERE Estado == :state AND Alimentacion != 'yes' AND Alimentacion != 'no'")
    suspend fun getPriceWorkState(state: String): List<SettingEntity>

    @Query(
        "SELECT con.Precio, sum(re.Cantidad) as result, sum(re.Cantidad * con.Precio) as total " +
                "FROM configuracion con  " +
                "INNER JOIN Recoleccion re ON con.PK_ID_Configuracion = re.Fk_Configuracion " +
                "WHERE re.Fecha >= :startDate AND re.Fecha <= :endDate AND con.Alimentacion LIKE :aliment"
    )
    suspend fun getTotalPdfWeek(startDate: String, endDate: String, aliment: String): List<totalPdf>

    @Query(
        "SELECT con.Precio, sum(wor.Cantidad) as result, sum(wor.Cantidad * con.Precio) as total " +
                "FROM configuracion con  " +
                "INNER JOIN WorkEntity wor ON con.PK_ID_Configuracion = wor.Fk_Configuracion " +
                "WHERE wor.Fecha >= :startDate AND wor.Fecha <= :endDate"
    )
    suspend fun getTotalPdfWeekWork(startDate: String, endDate: String): List<totalPdf>

    @Query(
        "SELECT con.Precio, sum(re.Cantidad) as cantidad, sum(re.Cantidad * con.Precio) as total " +
                "FROM configuracion con  " +
                "INNER JOIN Recoleccion re ON con.PK_ID_Configuracion = re.Fk_Configuracion " +
                "WHERE re.Estado == 'active'"
    )
    suspend fun getTotalCollectionActive(): List<totalWeekPdf>

    @Query(
        "SELECT con.Precio, sum(wor.cantidad) as cantidad, sum(wor.cantidad * con.Precio) as total " +
                "FROM configuracion con  " +
                "INNER JOIN WorkEntity wor ON con.PK_ID_Configuracion = wor.Fk_Configuracion " +
                "WHERE wor.Estado == 'active'"
    )
    suspend fun getTotalWorkActive(): List<totalWeekPdf>

    @Query("Delete FROM Configuracion")
    suspend fun delete()
}
