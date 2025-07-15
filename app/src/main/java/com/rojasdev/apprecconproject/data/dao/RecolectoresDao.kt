package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.dataModel.RecolectorConRecoleccionRaw
import com.rojasdev.apprecconproject.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.dataModel.collectionTotal
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.data.dataModel.pdfModel
import com.rojasdev.apprecconproject.data.dataModel.totalPdf
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity

@Dao
interface RecolectoresDao {

    @Insert
    suspend fun add(recolector: RecolectoresEntity)

    @Query(
        """
        SELECT DISTINCT * FROM recolectores
        WHERE estado_recolector == 'active'
        AND (
            name_recolector LIKE '%' || :searchQuery || '%' 
            OR id_temporal_en_recolector LIKE '%' || :searchQuery || '%'
        )
        """
    )
    fun searchCollectorsByName(searchQuery: String): List<RecolectoresEntity>

    @Query("UPDATE Recolectores SET  estado_recolector = 'archive' WHERE PK_ID_Recolector = :id")
    suspend fun updateCollectorState(id: Int)

    @Query("UPDATE Recolectores SET  name_recolector = :name WHERE PK_ID_Recolector = :id")
    suspend fun updateCollectorName(id: Int, name: String)

    @Query("Delete FROM recolectores WHERE PK_ID_Recolector LIKE :id")
    suspend fun deleteCollectorId(id: Int)

    @Query("Delete FROM recolectores")
    suspend fun delete()

    @Query("SELECT * FROM recolectores WHERE estado_recolector == 'active'")
    suspend fun getAllRecolector(): List<RecolectoresEntity>

    @Query("SELECT * FROM recolectores WHERE estado_recolector == 'work-active'")
    suspend fun getAllWorkMen(): List<RecolectoresEntity>

    @Query("SELECT PK_ID_Recolector FROM recolectores WHERE estado_recolector == 'active'")
    suspend fun getIDCollectors(): List<Long>

    @Query("SELECT PK_ID_Recolector FROM recolectores WHERE estado_recolector == 'work-active'")
    suspend fun getIDManWork(): List<Long>

    @Query("SELECT PK_ID_Recolector FROM recolectores")
    suspend fun getAll(): List<Long>

    @Query(
        "SELECT r.PK_ID_Recolector ,r.name_recolector, sum(re.Cantidad) " +
                "AS kg_collection, sum(re.Cantidad * c.Precio) AS price_total " +
                "FROM recolectores r " +
                "INNER JOIN recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
                "INNER JOIN configuracion c ON re.Fk_Configuracion = c.PK_ID_Configuracion " +
                "WHERE re.Fk_recolector == :collector AND re.Estado == 'active'"
    )
    suspend fun getCollectorAndCollectionTotal(collector: Int): List<collecionTotalCollector>

    @Query(
        "SELECT  SUM(re.Cantidad) AS total_kg, " +
                "con.Precio AS price, " +
                "con.Precio * SUM(re.Cantidad) AS price_total " +
                "FROM recolectores r " +
                "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
                "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
                "WHERE re.Estado == :state "
    )
    suspend fun getAllCollectorAndCollection(state: String): collectionTotal

    @Query(
        "SELECT r.PK_ID_Recolector, r.name_recolector, r.id_temporal_en_recolector, re.PK_ID_Recoleccion, re.Cantidad, " +
                "con.Precio * re.Cantidad AS result, con.Precio, " +
                "re.Estado, re.Fecha, re.Fk_Configuracion " +
                "FROM recolectores r " +
                "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
                "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
                "WHERE re.Estado == :state AND re.Fk_recolector LIKE :id  ORDER BY re.Fecha DESC"
    )
    suspend fun getCollectorAndCollection(state: String, id: Int): List<collectorCollection>

    @Query(
        "SELECT r.PK_ID_Recolector, r.name_recolector, re.PK_ID_Recoleccion, SUM(re.Cantidad) AS Cantidad, " +
                "SUM(re.Cantidad * con.Precio) AS result, con.Precio, " +
                "re.Estado, re.Fecha, re.Fk_Configuracion " +
                "FROM recolectores r " +
                "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
                "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
                "WHERE re.Fecha LIKE :dates AND r.PK_ID_Recolector == :id " +
                "ORDER BY re.Fecha DESC"
    )
    suspend fun getAllCollectorAndCollectionId(
        dates: String,
        id: Int
    ): List<allCollecionAndCollector>

    @Query(
        "SELECT r.PK_ID_Recolector, r.name_recolector, re.PK_ID_Recoleccion, sum(re.Cantidad) AS Cantidad, " +
                "sum(re.Cantidad * con.Precio) AS result, con.Precio, " +
                "re.Estado, re.Fecha,  re.Fk_Configuracion " +
                "FROM recolectores r " +
                "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
                "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
                "WHERE re.Fecha LIKE :dates ORDER BY re.Fecha DESC"
    )
    suspend fun getTotalKgDate(dates: String): List<allCollecionAndCollector>


    //<-------------------------------- CONSULTAS PARA PFD ---------------------------------------->

    //*PDF-MENSUAL

    //->Recoleccion

    @Query(
        "SELECT r.PK_ID_Recolector, r.name_recolector, re.Estado, " +
                "re.Fecha, re.Fk_Configuracion, " +
                "SUM(re.Cantidad) AS result, " +
                "SUM(con.Precio * re.Cantidad) AS total " +
                "FROM recolectores r " +
                "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
                "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
                "WHERE re.Fecha LIKE :date " +
                "GROUP BY re.Fk_recolector "
    )
    suspend fun getPdfInfo(date: String): List<pdfModel>

    @Query(
        "SELECT con.Precio, " +
                "SUM(re.Cantidad) AS result, " +
                "SUM(re.Cantidad * con.Precio) AS total " +
                "FROM recolectores r " +
                "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
                "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
                "WHERE re.Fecha LIKE :date "
    )
    suspend fun getTotalPdf(date: String): List<totalPdf>

//  SEMANAL

    @Query(
        "SELECT r.id_temporal_en_recolector AS id, " +
                "r.name_recolector AS name, " +
                "re.Fecha AS date, " +
                "re.Cantidad AS cantidad, " +
                "con.Precio AS price " +
                "FROM recolectores r " +
                "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
                "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
                "WHERE DATE(re.Fecha) BETWEEN :startDate AND :endDate "
        //"GROUP BY re.Fk_recolector " agrupar por estados a futur, tru, flase, pending
    )
    suspend fun getWeekExel(startDate: String, endDate: String): List<RecolectorConRecoleccionRaw>
}