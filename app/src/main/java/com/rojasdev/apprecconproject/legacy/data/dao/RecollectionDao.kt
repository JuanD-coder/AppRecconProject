package com.rojasdev.apprecconproject.legacy.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.legacy.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.legacy.data.dataModel.totalCollection

@Dao
interface RecollectionDao {

    @Insert
    suspend fun addRecollection(recoleccion: RecollectionEntity)

    @Query("SELECT Fecha FROM Recoleccion")
    suspend fun getDateCollection(): List<String>

    @Query("SELECT Fk_recolector FROM Recoleccion  WHERE Estado == 'active'")
    suspend fun getFkIdCollectors(): List<Long>

    @Query("UPDATE Recoleccion SET Cantidad = :kg, Fecha = :date,  Fk_Configuracion = :feed WHERE PK_ID_Recoleccion = :idCollection AND Fk_recolector = :idCollector")
    suspend fun updateCollection(idCollection: Int, date: String, idCollector: Int, kg: Double, feed: Int)

    @Query("UPDATE recoleccion SET estado = 'archive' WHERE Fk_recolector = :id")
    suspend fun updateCollectionState(id: Int)

    @Query("Delete FROM recoleccion")
    suspend fun delete()

    @Query(
        "SELECT sum(re.Cantidad) AS Cantidad, " +
                "sum(re.Cantidad * con.Precio) AS result " +
                "FROM Recoleccion re " +
                "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
                "WHERE re.Fecha LIKE :dates "
    )
    suspend fun getTotalKgDate(dates: String): totalCollection
}
