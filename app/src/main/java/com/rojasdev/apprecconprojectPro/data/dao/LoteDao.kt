package com.rojasdev.apprecconprojectPro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconprojectPro.data.dataModel.lotesAndCollection
import com.rojasdev.apprecconprojectPro.data.entities.FincaEntity
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity

@Dao
interface LoteDao {
    @Insert
    suspend fun add( lote : LoteEntity)

    @Insert
    suspend fun addM( lote : List<LoteEntity>)

    @Query("SELECT * FROM lote")
    suspend fun get(): List<LoteEntity>

    @Query("Delete FROM lote WHERE PK_ID_Lote LIKE :id")
    suspend fun deleteLoteId(id: Int)

    @Query("UPDATE lote SET  name_lote = :name WHERE PK_ID_Lote = :id")
    suspend fun updateLoteName(id:Int, name:String)

    @Query("UPDATE lote SET  state = :state WHERE PK_ID_Lote = :id")
    suspend fun updateLoteState(id:Int, state:Boolean)

    @Query("SELECT * FROM lote WHERE state == :state")
    suspend fun getNoFisebase(state: Boolean): List<LoteEntity>

    @Query("SELECT l.PK_ID_Lote, l.name_lote, sum(c.Cantidad) AS totalCollection " +
            "FROM lote l " +
            "INNER JOIN recoleccion c ON l.PK_ID_Lote = c.Fk_Lote" +
            " WHERE l.PK_ID_Lote like :id order by totalCollection desc")
    suspend fun getCollectionLote(id: Int): List<lotesAndCollection>

}