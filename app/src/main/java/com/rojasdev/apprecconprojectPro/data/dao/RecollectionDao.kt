package com.rojasdev.apprecconprojectPro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconprojectPro.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconprojectPro.data.dataModel.lotesAndCollection
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecollectionEntity

@Dao
interface RecollectionDao {

    @Insert
    suspend fun addRecollection(recoleccion:RecollectionEntity)

    @Insert
    suspend fun addM(recolector : List<RecollectionEntity>)

    @Query("UPDATE Recoleccion SET  estado_firebase = :state WHERE PK_ID_Recoleccion = :id")
    suspend fun updateCollectionStateFirebase(id:Int, state: Boolean)

    @Query("SELECT * FROM recoleccion WHERE estado_firebase == :state")
    suspend fun getAllCollectionFirebase(state: Boolean): List<RecollectionEntity>

    @Query("SELECT PK_ID_Recoleccion " +
            "FROM recoleccion " +
            "WHERE estado_firebase Like false "
    )
    suspend fun getCollectionStateFirebase(): List<Long>

    @Query("SELECT Fecha FROM Recoleccion")
    suspend fun getDateCollection(): List<String>

    @Query("SELECT Fk_recolector FROM Recoleccion  WHERE Estado == 'active'")
    suspend fun getFkIdCollectors(): List<Long>

    @Query("UPDATE Recoleccion SET Cantidad = :kg, Fecha = :date,  Fk_Configuracion = :feed WHERE PK_ID_Recoleccion = :idCollection AND Fk_recolector = :idCollector")
    suspend fun updateCollection(idCollection:Int, date:String, idCollector:Int, kg:Double, feed:Int)

    @Query("UPDATE recoleccion SET estado = 'archive' WHERE Fk_recolector = :id")
    suspend fun updateCollectionState(id:Int)

}