package com.rojasdev.apprecconprojectPro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconprojectPro.data.entities.FincaEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity

@Dao
interface FincaDao {

    @Insert
    suspend fun add( property : FincaEntity)

    @Query("SELECT * FROM finca")
    suspend fun get(): List<FincaEntity>

    @Query("SELECT PK_ID_Finca FROM finca")
    suspend fun getId(): Int

    @Query("UPDATE finca SET name_finca = :newName WHERE PK_ID_Finca == :id")
    suspend fun update(id: Int?, newName: String)
}