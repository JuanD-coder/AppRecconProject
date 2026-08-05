package com.rojasdev.apprecconproject.data.repository

import com.rojasdev.apprecconproject.domain.model.DailyAnalyticsData
import com.rojasdev.apprecconproject.domain.repository.AnalyticsRepository
import com.rojasdev.apprecconproject.legacy.data.dao.RecolectoresDao
import com.rojasdev.apprecconproject.legacy.data.dao.RecollectionDao
import com.rojasdev.apprecconproject.legacy.data.dao.WorkDao
import com.rojasdev.apprecconproject.legacy.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.legacy.data.dataModel.allWorkAndCollector
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val recolectoresDao: RecolectoresDao,
    private val recollectionDao: RecollectionDao,
    private val workDao: WorkDao
) : AnalyticsRepository {

    override suspend fun getDailyData(date: String): DailyAnalyticsData {
        val collectionRecords = mutableListOf<allCollecionAndCollector>()
        val workRecords = mutableListOf<allWorkAndCollector>()
        
        val allIds = recolectoresDao.getAll()
        for (id in allIds) {
            val coll = recolectoresDao.getAllCollectorAndCollectionId("$date%", id.toInt())
            if (coll.isNotEmpty() && coll[0].name_recolector != null) {
                collectionRecords.add(coll[0])
            }
            
            val work = recolectoresDao.getAllCollectorAndWorkId("$date%", id.toInt())
            if (work.isNotEmpty() && work[0].name_recolector != null) {
                workRecords.add(work[0])
            }
        }

        val totalColl = recollectionDao.getTotalKgDate("$date%")
        val totalWor = workDao.getTotalWorkDate("$date%")

        return DailyAnalyticsData(
            collectionRecords = collectionRecords,
            workRecords = workRecords,
            totalKg = totalColl.Cantidad,
            totalCollectionMoney = totalColl.result,
            totalWorkDays = totalWor.Cantidad,
            totalWorkMoney = totalWor.result
        )
    }

    override suspend fun getDatesWithCollection(): List<String> {
        return recollectionDao.getDateCollection().map { it.substring(0, 10) }
    }

    override suspend fun getDatesWithWork(): List<String> {
        return workDao.getDateWork().map { it.substring(0, 10) }
    }
}
