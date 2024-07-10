package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.data.entities.WorkEntity

@Dao
interface WorkDao {

    @Insert
    suspend fun insert(config: WorkEntity)

    @Query("SELECT Fk_recolector FROM WorkEntity  WHERE Estado == 'active'")
    suspend fun getFkIdCollectors(): List<Long>
}