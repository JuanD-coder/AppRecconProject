package com.rojasdev.apprecconproject.data.repository

import com.rojasdev.apprecconproject.legacy.data.dao.RecolectoresDao
import com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface CollectorRepository {
    suspend fun getCollectors(): List<RecolectoresEntity>
}

class CollectorRepositoryImpl @Inject constructor(
    private val recolectoresDao: RecolectoresDao
) : CollectorRepository {
    override suspend fun getCollectors(): List<RecolectoresEntity> {
        return recolectoresDao.getAllRecolector()
    }
}
