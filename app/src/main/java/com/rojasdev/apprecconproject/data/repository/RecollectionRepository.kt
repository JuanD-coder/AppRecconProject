package com.rojasdev.apprecconproject.data.repository

import com.rojasdev.apprecconproject.legacy.data.dao.RecollectionDao
import com.rojasdev.apprecconproject.legacy.data.entities.RecollectionEntity
import javax.inject.Inject

interface RecollectionRepository {
    suspend fun addRecollection(recollection: RecollectionEntity)
    suspend fun updateCollection(idCollection: Int, date: String, idCollector: Int, kg: Double, feed: Int)
    suspend fun archiveCollection(idCollector: Int)
}

class RecollectionRepositoryImpl @Inject constructor(
    private val recollectionDao: RecollectionDao
) : RecollectionRepository {
    override suspend fun addRecollection(recollection: RecollectionEntity) {
        recollectionDao.addRecollection(recollection)
    }

    override suspend fun updateCollection(idCollection: Int, date: String, idCollector: Int, kg: Double, feed: Int) {
        recollectionDao.updateCollection(idCollection, date, idCollector, kg, feed)
    }

    override suspend fun archiveCollection(idCollector: Int) {
        recollectionDao.updateCollectionState(idCollector)
    }
}
