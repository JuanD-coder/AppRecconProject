package com.rojasdev.apprecconproject.domain.usecase

import com.rojasdev.apprecconproject.data.repository.CollectorRepository
import com.rojasdev.apprecconproject.legacy.data.dao.RecolectoresDao
import com.rojasdev.apprecconproject.legacy.data.dataModel.collecionTotalCollector
import javax.inject.Inject

class GetCollectorsWithTotalUseCase @Inject constructor(
    private val recolectoresDao: RecolectoresDao
) {
    suspend operator fun invoke(): List<collecionTotalCollector> {
        val ids = recolectoresDao.getIDCollectors()
        val result = mutableListOf<collecionTotalCollector>()
        for (id in ids) {
            val total = recolectoresDao.getCollectorAndCollectionTotal(id.toInt())
            if (total.isNotEmpty() && total[0].name_recolector != null) {
                result.add(total[0])
            }
        }
        return result
    }
}
